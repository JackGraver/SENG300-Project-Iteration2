public class DebitCardTest{
    @Test
    public void testCardDataType() {
        // Create an instance of the class that contains the cardDataType method
        YourClassName instance = new YourClassName();

        // Test case 1: Ensure the method works with a consistent output
        CardData consistentData = new CardData("Visa");
        String result1 = instance.cardDataType(consistentData);
        assertEquals("Visa", result1);

        // Test case 2: Ensure the method works with an inconsistent output
        CardData inconsistentData = new CardData("MasterCard");
        String result2 = instance.cardDataType(inconsistentData);
        assertNotEquals("Visa", result2);

        // Test case 3: Ensure the method handles null input gracefully
        CardData nullData = null;
        String result3 = instance.cardDataType(nullData);
        assertNull(result3);

        // Test case 4: Ensure the method handles different types of cards
        CardData amexData = new CardData("AmericanExpress");
        String result4 = instance.cardDataType(amexData);
        assertEquals("AmericanExpress", result4);
        
        // Test case 5: Ensure the method works with a mix of card types
        CardData mixData = new CardData("Visa");
        String result5 = instance.cardDataType(mixData);
        assertEquals("Visa", result5);
    };
}