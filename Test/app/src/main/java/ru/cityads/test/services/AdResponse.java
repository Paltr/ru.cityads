package ru.cityads.test.services;

/**
 * Represents response to ad request, made by {@link AdsService}
 *
 * @see AdsService
 */
@SuppressWarnings({"ALL", "unused"})
public final class AdResponse
{
    private String status;
    private String message;
    private String url;

    public String getMessage() { return message; }
    public String getUrl() { return url; }

    public boolean checkStatus() { return status.equals("OK"); }
}
