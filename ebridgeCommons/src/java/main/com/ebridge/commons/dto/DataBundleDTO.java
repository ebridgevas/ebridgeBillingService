package com.ebridge.commons.dto;

import java.math.BigDecimal;

/**
 * @author david@tekeshe.com
 */
public class DataBundleDTO implements Comparable<DataBundleDTO> {

    private final String productType;
    private final String bundleType;
    private final String shortDescription;
    private final String bundleDescription;
    private final BigDecimal bundleSize;
    private final BigDecimal bundleRate;
    private final BigDecimal debit;
    private final BigDecimal credit;
    private final BigDecimal consumptionTariff;
    private final Integer windowSize;

    public DataBundleDTO (
            String productType,
            String bundleType,
            String shortDescription,
            String bundleDescription,
            BigDecimal bundleSize,
            BigDecimal bundleRate,
            BigDecimal debit,
            BigDecimal credit,
            BigDecimal consumptionTariff,
            Integer windowSize) {
        this.productType = productType;
        this.bundleType = bundleType;
        this.shortDescription = shortDescription;
        this.bundleDescription = bundleDescription;
        this.bundleSize = bundleSize;
        this.bundleRate = bundleRate;
        this.debit = debit;
        this.credit = credit;
        this.consumptionTariff = consumptionTariff;
        this.windowSize = windowSize;
    }

    public String getProductType() {
        return productType;
    }

    public String getBundleType() {
        return bundleType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getBundleDescription() {
        return bundleDescription;
    }

    public BigDecimal getBundleSize() {
        return bundleSize;
    }

    public BigDecimal getBundleRate() {
        return bundleRate;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public BigDecimal getConsumptionTariff() {
        return consumptionTariff;
    }

    public Integer getWindowSize() {
        return windowSize;
    }

    @Override
    public int compareTo(DataBundleDTO o) {
        return bundleType.compareTo( o.getBundleType() );
    }
}
