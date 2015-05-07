package org.uengine.codi.mw3.knowledge;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.annotation.Test;

public class MashupGoogleImage {

	@Test(scenario="first", starter=true, instruction="동적으로 검색된 이미지들입니다. 마음에 드는 이미지를 더블 클릭하세요 ...", next="autowiredObject.org.uengine.codi.mw3.knowledge.WfPanel.load()")
	@ServiceMethod
	public void putImage(){}
	
}
