package vn.edu.stu.doannguonmo.model;

import java.io.Serializable;

public class trasua implements Serializable {
    private int ma;
    private String ten;
    private String phanloai;
    private byte [] hinhanh;
    private String gia;
    private String soluong;

    public trasua() {
    }

    public trasua(int ma, String ten, String phanloai, byte[] hinhanh, String gia, String soluong) {
        this.ma = ma;
        this.ten = ten;
        this.phanloai = phanloai;
        this.hinhanh = hinhanh;
        this.gia = gia;
        this.soluong = soluong;
    }

    public int getMa() {
        return ma;
    }

    public void setMa(int ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getPhanloai() {
        return phanloai;
    }

    public void setPhanloai(String phanloai) {
        this.phanloai = phanloai;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }
}