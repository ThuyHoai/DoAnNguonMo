package vn.edu.stu.doannguonmo.model;

public class danhmuc extends trasua{
    private String maDM;
    private String tenDM;

    public danhmuc() {
    }

    public danhmuc(String maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getTenDM() {
        return tenDM;
    }

    public void setTenDM(String tenDM) {
        this.tenDM = tenDM;
    }

    @Override
    public String toString() {
        return maDM + "_" + tenDM;
    }
}