package ru.kmpo.camviewer.dto;

public final class Views {
    public interface withoutCamera {}
    public interface withCamera extends withoutCamera{}
    public interface camera extends withCamera{}
}
