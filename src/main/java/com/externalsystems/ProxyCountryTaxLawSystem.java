package com.externalsystems;

public class ProxyCountryTaxLawSystem implements ICountryTaxLawSystem{

    private ICountryTaxLawSystem countryTaxLawSystem = new CountryTaxLawSystem();

    @Override
    public double getTaxRate(double revenueAmount) {
        return countryTaxLawSystem.getTaxRate(revenueAmount);
    }
}
