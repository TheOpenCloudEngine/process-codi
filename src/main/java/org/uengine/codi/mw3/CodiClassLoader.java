package org.uengine.codi.mw3;

import org.codehaus.commons.compiler.AbstractJavaSourceClassLoader;
import org.codehaus.commons.compiler.jdk.ByteArrayJavaFileManager;
import org.codehaus.commons.compiler.jdk.ByteArrayJavaFileManager.ByteArrayJavaFileObject;
import org.metaworks.MetaworksException;
import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.uengine.kernel.GlobalContext;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class CodiClassLoader extends AbstractJavaSourceClassLoader {

	public final static String DEFAULT_NAME = "default";
	public final static String PATH_SEPARATOR = "/";

	String						codebase;

	private File[]             sourcePath;
	private String             optionalCharacterEncoding;
	private boolean            debuggingInfoLines;
	private boolean            debuggingInfoVars;
	private boolean            debuggingInfoSource;
	private Collection<String> compilerOptions = new ArrayList<String>();

	private JavaCompiler    compiler;
	private JavaFileManager fileManager;

	public static CodiClassLoader codiClassLoader; //works for default classloader.


	/**
	 * @see org.codehaus.commons.compiler.ICompilerFactory#newJavaSourceClassLoader()
	 */
	public CodiClassLoader() {
		this.init();
	}

	/**
	 * @see org.codehaus.commons.compiler.ICompilerFactory#newJavaSourceClassLoader(ClassLoader)
	 */
	public CodiClassLoader(ClassLoader parentClassLoader) {
		super(parentClassLoader);
		this.init();
	}

	public String getCodebase() {
		return codebase;
	}
	public void setCodebase(String codebase) {
		this.codebase = codebase;
	}

	public static CodiClassLoader getMyClassLoader(){
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		return (CodiClassLoader) contextClassLoader;
	}

	public static String getCodeBaseRoot() {
        return GlobalContext.getPropertyString("codebase") + File.separator;
	}

	@Override
	public InputStream getResourceAsStream(String name) {

		if(name != null){
			//if(name.endsWith(".ejs") || name.endsWith(".ejs.js") || name.endsWith(".xml") || name.endsWith(".process") || name.endsWith(".process2") || name.endsWith(".sql") || name.endsWith(".wpd") || name.endsWith("metadata")){

			for(File file : this.sourcePath){
				try {
					if(file.exists()){
						FileInputStream fis = new FileInputStream(file.getAbsolutePath() + File.separator + name);
						return fis;
					}
				} catch (FileNotFoundException e) {
				}
			}
			//}
		}

		return super.getResourceAsStream(name);
	}

	private void init() {
		this.compiler = ToolProvider.getSystemJavaCompiler();
		if (this.compiler == null) {
			throw new UnsupportedOperationException(
					"JDK Java compiler not available - probably you're running a JRE, not a JDK"
					);
		}
	}

	/**
	 * Creates the underlying {@link javax.tools.JavaFileManager} lazily, because {@link #setSourcePath(java.io.File[])} and consorts
	 * are called <i>after</i> initialization.
	 */
	public JavaFileManager getJavaFileManager() {
		if (this.fileManager == null) {

			// Get the original FM, which reads class files through this JVM's BOOTCLASSPATH and
			// CLASSPATH.
			JavaFileManager jfm = this.compiler.getStandardFileManager(null, null, null);

			// Wrap it so that the output files (in our case class files) are stored in memory rather
			// than in files.
			jfm = new ByteArrayJavaFileManager<JavaFileManager>(jfm);

			// Wrap it in a file manager that finds source files through the source path.
			jfm = new FileInputJavaFileManager(
					jfm,
					StandardLocation.SOURCE_PATH,
					Kind.SOURCE,
					this.sourcePath,
					this.optionalCharacterEncoding
					);

			this.fileManager = jfm;
		}
		return this.fileManager;
	}

	@Override
	public void setSourcePath(File[] sourcePath) {
		this.sourcePath = sourcePath;
	}
	
	public void addSourcePath(File addSourcePath) {
		File[] ref = new File[this.sourcePath.length+1];
		System.arraycopy(this.sourcePath, 0, ref, 0, this.sourcePath.length);
		ref[this.sourcePath.length] = addSourcePath;
		this.sourcePath = ref;
	}

	@Override
	public void setSourceFileCharacterEncoding(String optionalCharacterEncoding) {
		this.optionalCharacterEncoding = optionalCharacterEncoding;
	}

	@Override
	public void setDebuggingInfo(boolean lines, boolean vars, boolean source) {
		this.debuggingInfoLines  = lines;
		this.debuggingInfoVars   = vars;
		this.debuggingInfoSource = source;
	}

	/**
	 * Notice: Don't use the '-g' options - these are controlled through {@link #setDebuggingInfo(boolean, boolean,
	 * boolean)}.
	 *
	 * @param compilerOptions All command line options supported by the JDK JAVAC tool
	 */
	public void setCompilerOptions(String[] compilerOptions) {
		this.compilerOptions = Arrays.asList(compilerOptions);
	}

	/**
	 * Implementation of {@link ClassLoader#findClass(String)}.
	 *
	 * @throws ClassNotFoundException
	 */
	protected Class<?> findClass(String className) throws ClassNotFoundException {


		//TODO: it looks bad so, we switch to use HotSwapper in the javassist

		byte[] ba;
		int    size;
		try {

			// Maybe the bytecode is already there, because the class was compiled as a side effect of a preceding
			// compilation.
			JavaFileObject classFileObject = this.getJavaFileManager().getJavaFileForInput(
					StandardLocation.CLASS_OUTPUT,
					className,
					Kind.CLASS
					);

			if (classFileObject == null) {

				// Get the sourceFile.
				JavaFileObject sourceFileObject = this.getJavaFileManager().getJavaFileForInput(
						StandardLocation.SOURCE_PATH,
						className,
						Kind.SOURCE
						);


				if (sourceFileObject == null) {
					//                   throw new DiagnosticException("Source for '" + className + "' not found");
					throw new ClassNotFoundException("Source for '" + className + "' not found");

					//return null;
				}

				// Compose the effective compiler options.
				List<String> options = new ArrayList<String>(this.compilerOptions);
				options.add(this.debuggingInfoLines ? (
						this.debuggingInfoSource ? (
								this.debuggingInfoVars
								? "-g"
										: "-g:lines,source"
								) : this.debuggingInfoVars ? "-g:lines,vars" : "-g:lines"
						) : this.debuggingInfoSource ? (
								this.debuggingInfoVars
								? "-g:source,vars"
										: "-g:source"
								) : this.debuggingInfoVars ? "-g:vars" : "-g:none");

				//  this.compiler

				// Run the compiler.
				if (!this.compiler.getTask(
						null,                                   // out
						this.getJavaFileManager(),              // fileManager
						new DiagnosticListener<JavaFileObject>() { // diagnosticListener

							@Override
							public void report(final Diagnostic<? extends JavaFileObject> diagnostic) {
								if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
									throw new DiagnosticException(diagnostic);
								}
							}
						},
						options,                                // options
						null,                                   // classes
						Collections.singleton(sourceFileObject) // compilationUnits
						).call()) {
					throw new ClassNotFoundException(className + ": Compilation failed");
				}

				classFileObject = this.getJavaFileManager().getJavaFileForInput(
						StandardLocation.CLASS_OUTPUT,
						className,
						Kind.CLASS
						);

				if (classFileObject == null) {
					throw new ClassNotFoundException(className + ": Class file not created by compilation");
				}
			}

			if (classFileObject instanceof ByteArrayJavaFileObject) {
				ByteArrayJavaFileObject bajfo = (ByteArrayJavaFileObject) classFileObject;
				ba = bajfo.toByteArray();
				size = ba.length;
			} else
			{
				ba = new byte[4096];
				size = 0;
				InputStream is = classFileObject.openInputStream();
				try {
					for (;;) {
						int res = is.read(ba, size, ba.length - size);
						if (res == -1) break;
						size += res;
						if (size == ba.length) {
							byte[] tmp = new byte[2 * size];
							System.arraycopy(ba, 0, tmp, 0, size);
							ba = tmp;
						}
					}
				} finally {
					is.close();
				}
			}
		} catch (IOException ioe) {
			throw new DiagnosticException(ioe);
		}

		return this.defineClass(className, ba, 0, size, (
				this.optionalProtectionDomainFactory == null
				? null
						: this.optionalProtectionDomainFactory.getProtectionDomain(getSourceResourceName(className))
				));
	}

	/**
	 * Construct the name of a resource that could contain the source code of
	 * the class with the given name.
	 * <p>
	 * Notice that member types are declared inside a different type, so the relevant source file
	 * is that of the outermost declaring class.
	 *
	 * @param className Fully qualified class name, e.g. "pkg1.pkg2.Outer$Inner"
	 * @return the name of the resource, e.g. "pkg1/pkg2/Outer.java"
	 */
	private static String getSourceResourceName(String className) {

		// Strip nested type suffixes.
		{
			int idx = className.lastIndexOf('.') + 1;
			idx = className.indexOf('$', idx);
			if (idx != -1) className = className.substring(0, idx);
		}

		return className.replace('.', '/') + ".java";
	}

	public static class DiagnosticException extends RuntimeException {

		private static final long serialVersionUID = 5589635876875819926L;

		public DiagnosticException(String message) {
			super(message);
		}

		public DiagnosticException(Throwable cause) {
			super(cause);
		}

		public DiagnosticException(Diagnostic<? extends JavaFileObject> diagnostic) {
			super(diagnostic.toString());
		}
	}
	
	public static CodiClassLoader createClassLoader(String projectId){
		CodiClassLoader cl = new CodiClassLoader(CodiMetaworksRemoteService.class.getClassLoader());

		if( CodiMetaworksRemoteService.class.getClassLoader() instanceof URLClassLoader ){

			URLClassLoader classLoader = (URLClassLoader) CodiMetaworksRemoteService.class.getClassLoader();
			URL urls[] = classLoader.getURLs();
			StringBuffer sbClasspath = new StringBuffer();
			for(URL url : urls){
				String urlStr = url.getFile().toString();
				sbClasspath.append(urlStr).append(File.pathSeparator);
			}

			cl.setCompilerOptions(
					new String[]{
							"-classpath", sbClasspath.toString()
					});
		}

		List<File> sourcePath = new ArrayList<File>();

		// add project source path
		if(projectId != null){
			sourcePath.add(new File(getCodeBaseRoot() + getProjectPathOfTenant(projectId)));
		}

		// add codi source path
        sourcePath.add(new File(getCodeBaseRoot() + getProjectPathOfTenant(getCodiId())));

		cl.setCodebase(sourcePath.get(0).getAbsolutePath());
		cl.setSourcePath(sourcePath.toArray(new File[sourcePath.size()]));

		return cl;
	}  

    public static String getCodiId(){
        return "codi";
    }

    public static String getProjectPathOfTenant(String projectId){
        String tenantId = TenantContext.getThreadLocalInstance().getTenantId();

        tenantId = (tenantId == null ? "default" : tenantId);

        return projectId + File.separator + tenantId + File.separator;
    }

	public static void refreshClassLoader(String resourceName){

		try {
			MetaworksRemoteService.getInstance().clearMetaworksType(resourceName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void initClassLoader(){
		if(codiClassLoader==null)
			refreshClassLoader(null);

		Thread.currentThread().setContextClassLoader(codiClassLoader);
	}

}