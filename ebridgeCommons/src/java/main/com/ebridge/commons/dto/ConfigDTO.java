package com.ebridge.commons.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author david@tekeshe.com
 */
public class ConfigDTO {

    private Map<String, LanguageDTO> balanceNames;
    private Map<String, List<String>> cosBalances;
    private List<String> postpaidCos;
    private Map<String, DataBundleDTO> dataBundles;
    private Map<String, BigDecimal> balanceTransferParameters;

    public Map<String, LanguageDTO> getBalances() {
        return balanceNames;
    }

    public void setBalances(Map<String, LanguageDTO> balanceNames) {
        this.balanceNames = balanceNames;
    }

    public Map<String, LanguageDTO> getBalanceNames() {
        return balanceNames;
    }

    public void setBalanceNames(Map<String, LanguageDTO> balanceNames) {
        this.balanceNames = balanceNames;
    }

    public Map<String, List<String>> getCosBalances() {
        return cosBalances;
    }

    public void setCosBalances(Map<String, List<String>> cosBalances) {
        this.cosBalances = cosBalances;
    }

    public List<String> getPostpaidCos() {
        return postpaidCos;
    }

    public void setPostpaidCos(List<String> postpaidCos) {
        this.postpaidCos = postpaidCos;
    }

    public Map<String, DataBundleDTO> getDataBundles() {
        return dataBundles;
    }

    public void setDataBundles(Map<String, DataBundleDTO> dataBundles) {
        this.dataBundles = dataBundles;
    }

    public Map<String, BigDecimal> getBalanceTransferParameters() {
        return balanceTransferParameters;
    }

    public void setBalanceTransferParameters(Map<String, BigDecimal> balanceTransferParameters) {
        this.balanceTransferParameters = balanceTransferParameters;
    }
}
