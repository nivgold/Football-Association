package com.externalsystems;

public class CountryTaxLawSystem implements ICountryTaxLawSystem{

    @Override
    public double getTaxRate(double revenueAmount) {
        if (revenueAmount > 1000000)
            return 0.2;
        else if (revenueAmount > 500000)
            return 0.15;
        else
            return 0.1;
    }
}
