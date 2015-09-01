/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editoprivate String 
 */
package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author Poolsawat.a
 */
public class ObjProgramVersion {

    private int version_id;
    private String version_name;
    private String version_desc;
    private String version_updatedate;
    private String version_updateby;

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public String getVersion_name() {
        return StringOpUtil.removeNull(version_name);
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getVersion_desc() {
        return StringOpUtil.removeNull(version_desc);
    }

    public void setVersion_desc(String version_desc) {
        this.version_desc = version_desc;
    }

    public String getVersion_updatedate() {
        return StringOpUtil.removeNull(version_updatedate);
    }

    public void setVersion_updatedate(String version_updatedate) {
        this.version_updatedate = version_updatedate;
    }

    public String getVersion_updateby() {
        return StringOpUtil.removeNull(version_updateby);
    }

    public void setVersion_updateby(String version_updateby) {
        this.version_updateby = version_updateby;
    }

    @Override
    public String toString() {
        return "ObjProgramVersion{" + "version_id=" + version_id + ", version_name=" + version_name + ", version_desc=" + version_desc + ", version_updatedate=" + version_updatedate + ", version_updateby=" + version_updateby + '}';
    }

}
