package DomainLayer;

import java.util.Date;

public class CreditCard {
    private int cardNum;

    private Date expiryDate;

    private String cardName;

    private int securityCode;

    public CreditCard(int cNum, Date eDate, String cName, int sCode) {
        this.cardNum = cNum;
        this.expiryDate = eDate;
        this.cardName = cName;
        this.securityCode = sCode;
    }

    public int getCardNum() {
        return cardNum;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getCardName() {
        return cardName;
    }

    public int getSecurityCode() {
        return securityCode;
    }
}
