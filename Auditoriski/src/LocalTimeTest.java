public class LocalTimeTest {
    public static void main(String[] args) {
        Test t = new Test(){
            @Override
            public void modifiedPrint() {
                System.out.println("Anonymous class 1");
            }
        };

        Test s = new Test() {

            @Override
            public void modifiedPrint() {
                System.out.println("Anonymous class 2");
            }
        };

        t.modifiedPrint();
        s.modifiedPrint();


    }
}

interface Test {
    void modifiedPrint();
}



