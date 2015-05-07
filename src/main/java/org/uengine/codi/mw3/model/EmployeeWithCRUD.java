package org.uengine.codi.mw3.model;

import org.metaworks.*;

/**
 * Created by ho.lim on 2015-04-22.
 */
public class EmployeeWithCRUD extends AbstractEmployee{
    public IEmployee load() throws Exception {
        String errorMessage = null;
        if (getEmail() != null) {
            IEmployee emp = (IEmployee) findByEmail();

            if (emp.getIsDeleted() != null && emp.getIsDeleted().equals("1")) {
                errorMessage = "<font color=blue>There's no such ID. Please subscribe.</font>";
            }
            if (getPassword().equals(emp.getPassword())) {
                EmployeeWithCRUD employee = new EmployeeWithCRUD();
                employee.copyFrom(emp);
                emp = employee.findMe();

                getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
                return emp;
            } else {
                errorMessage = "<font color=blue>Wrong User or Password! forgot?</font>";
            }
        } else {
            errorMessage = "<font color=blue>There's no such ID. Please subscribe.</font>";
        }
        throw new Exception(errorMessage);
    }

    public IEmployee findMe() throws Exception {
        EmployeeWithCRUD employee = new EmployeeWithCRUD();

        StringBuffer sb = new StringBuffer();
        sb.append("select emptable.*, PARTTABLE.PARTNAME from ");
        sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
        sb.append("where emptable.empcode=?empcode ");

        IEmployee findEmployee = (IEmployee) sql(sb.toString());
        findEmployee.set("empcode", this.getEmpCode());
        findEmployee.select();
        if (findEmployee.next()) {
            employee.copyFrom(findEmployee);
            employee.getDept().getMetaworksContext().setHow("picker");
        }
        return employee;
    }

    public IEmployee findMeByEmpName() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("select emptable.*, PARTTABLE.PARTNAME from ");
        sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
        sb.append("where emptable.EMPNAME like ?empName ");

        IEmployee findEmployee = (IEmployee) sql(sb.toString());
        findEmployee.set("empName", "%" + getEmpName() + "%");
        findEmployee.select();
        findEmployee.setMetaworksContext(this.getMetaworksContext());

        return findEmployee;
    }
    
    public IEmployee findByDept(Dept dept) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("select emptable.*, PARTTABLE.PARTNAME from ");
        sb.append("emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
        if (dept.getPartCode() != null) {
            sb.append("where emptable.partcode=?partCode ");
        } else {
            sb.append("where emptable.partcode is null ");
        }
        sb.append("and emptable.globalcom=?globalCom ");
        IEmployee deptEmployee = sql(sb.toString());
        if (dept.getPartCode() != null) {
            deptEmployee.setPartCode(dept.getPartCode());
        }
        deptEmployee.setGlobalCom(dept.getGlobalCom());
        deptEmployee.select();
        deptEmployee.setMetaworksContext(this.getMetaworksContext());

        return deptEmployee;
    }
    
    public IEmployee findByDeptOther(String empCode) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from emptable e");
        sb.append(" where e.globalcom=?globalCom");
        sb.append(" and e.empcode not in");
        sb.append(" (select c.friendid as empcode from contact c");
        sb.append(" where c.userId=?empCode)");
        sb.append(" and e.empcode!=?empCode");

        IEmployee deptEmployee = sql(sb.toString());
        deptEmployee.setGlobalCom(this.getGlobalCom());
        deptEmployee.setEmpCode(empCode);
        deptEmployee.select();
        deptEmployee.setMetaworksContext(this.getMetaworksContext());

        return deptEmployee;
    }

    public IEmployee findByRole(Role role) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT a.*");
        sb.append("  FROM emptable a, roleUserTable b");
        sb.append(" WHERE a.empCode = b.empCode");
        sb.append("   AND a.isDeleted=?isDeleted");
        sb.append("   AND b.roleCode=?roleCode");

        IEmployee employee = sql(sb.toString());
        employee.setIsDeleted("0");
        employee.set("roleCode", role.getRoleCode());
        employee.select();
        employee.setMetaworksContext(this.getMetaworksContext());

        return employee;
    }

    public IEmployee findByGlobalCom(String GlobalCom) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * ");
        sb.append("FROM emptable ");
        sb.append("WHERE globalCom=?GlobalCom");

        IEmployee employee = sql(sb.toString());
        employee.set("GlobalCom", GlobalCom);
        employee.select();
        employee.setMetaworksContext(this.getMetaworksContext());

        return employee;
    }
    
    public IEmployee findByEmail() throws Exception {
        EmployeeWithCRUD employee = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select emptable.*, PARTTABLE.PARTNAME");
        sb.append("  from emptable LEFT OUTER JOIN PARTTABLE on emptable.partcode=PARTTABLE.partcode ");
        sb.append(" where emptable.email=?email ");
        sb.append("   and emptable.isdeleted = 0");

        IEmployee findEmployee = (IEmployee) sql(sb.toString());
        findEmployee.setEmail(this.getEmail());
        findEmployee.select();
        if (findEmployee.next()) {
            employee = new EmployeeWithCRUD();
            employee.copyFrom(findEmployee);
            employee.getDept().getMetaworksContext().setHow("picker");
        }
        return employee;
    }
    
    public IEmployee findForLogin() {
        StringBuffer sb = new StringBuffer();
        sb.append("select empcode, password, globalcom");
        sb.append("  from emptable");
        sb.append(" where email=?email ");
        sb.append("   and isdeleted = 0");

        IEmployee dao = null;

        try {
            dao = sql(sb.toString());
            dao.setEmail(this.getEmail());
            dao.select();

            if (!dao.next())
                dao = null;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dao;
    }
    
    public IEmployee findByKey() {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ");
        sb.append("emptable ");
        sb.append("where authKey=?authKey ");

        IEmployee dao = null;

        try {
            dao = sql(sb.toString());
            dao.setAuthKey(this.getAuthKey());
            dao.select();

            if (!dao.next())
                dao = null;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dao;
    }

    public IUser getUser() {
        User user = new User();
        user.session = session;
        user.setUserId(this.getEmpCode());
        user.setName(this.getEmpName());

        return user;
    }
}
