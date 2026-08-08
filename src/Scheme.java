// One row from the Schemes database table.
public class Scheme {
    private int schemeId;
    private String schemeName;
    private int minAge;
    private int maxAge;
    private double maxIncome;
    private String gender;
    private String occupation;
    private String category;
    private int stateId;
    private String description;

    public Scheme(int schemeId, String schemeName, int minAge, int maxAge,
            double maxIncome, String gender, String occupation, String category,
            int stateId, String description) {
        this.schemeId = schemeId;
        this.schemeName = schemeName;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.maxIncome = maxIncome;
        this.gender = gender;
        this.occupation = occupation;
        this.category = category;
        this.stateId = stateId;
        this.description = description;
    }

    public int getSchemeId() { return schemeId; }
    public void setSchemeId(int schemeId) { this.schemeId = schemeId; }
    public String getSchemeName() { return schemeName; }
    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    public int getMinAge() { return minAge; }
    public void setMinAge(int minAge) { this.minAge = minAge; }
    public int getMaxAge() { return maxAge; }
    public void setMaxAge(int maxAge) { this.maxAge = maxAge; }
    public double getMaxIncome() { return maxIncome; }
    public void setMaxIncome(double maxIncome) { this.maxIncome = maxIncome; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getStateId() { return stateId; }
    public void setStateId(int stateId) { this.stateId = stateId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
