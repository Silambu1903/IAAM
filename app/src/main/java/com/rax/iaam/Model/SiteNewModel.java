package com.rax.iaam.Model;

public class SiteNewModel {
    int siteId;
    String siteName;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public SiteNewModel(int siteId, String siteName) {
        this.siteId = siteId;
        this.siteName = siteName;
    }
}
