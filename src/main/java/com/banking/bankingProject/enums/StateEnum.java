package com.banking.bankingProject.enums;

import lombok.Getter;

@Getter
public enum StateEnum {
    ANDHRA_PRADESH("Andhra Pradesh"),
    ARUNACHAL_PRADESH("Arunachal Pradesh"),
    ASSAM("Assam"),
    BIHAR("Bihar"),
    CHHATTISGARH("Chhattisgarh"),
    GOA("Goa"),
    GUJARAT("Gujarat"),
    HARYANA("Haryana"),
    HIMACHAL_PRADESH("Himachal Pradesh"),
    JHARKHAND("Jharkhand"),
    KARNATAKA("Karnataka"),
    KERALA("Kerala"),
    MADHYA_PRADESH("Madhya Pradesh"),
    MAHARASHTRA("Maharashtra"),
    MANIPUR("Manipur"),
    MEGHALAYA("Meghalaya"),
    MIZORAM("Mizoram"),
    NAGALAND("Nagaland"),
    ODISHA("Odisha"),
    PUNJAB("Punjab"),
    RAJASTHAN("Rajasthan"),
    SIKKIM("Sikkim"),
    TAMIL_NADU("Tamil Nadu"),
    TELANGANA("Telangana"),
    TRIPURA("Tripura"),
    UTTAR_PRADESH("Uttar Pradesh"),
    UTTARAKHAND("Uttarakhand"),
    WEST_BENGAL("West Bengal"),
    ANDAMAN_NICOBAR("Andaman and Nicobar Islands"),
    CHANDIGARH("Chandigarh"),
    DADRA_NAGAR_HAVELI("Dadra and Nagar Haveli and Daman and Diu"),
    DAMAN_AND_DIU("Daman and Diu"),
    LAKSHADWEEP("Lakshadweep"),
    DELHI("Delhi"),
    PUDUCHERRY("Puducherry");


    private final String state;

    StateEnum(String state) {
        this.state = state;
    }

}
