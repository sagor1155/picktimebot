
import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate   = LocalDate.now().plusDays(30);
        SlotFinder slotFinder = new SlotFinder();
        // TODO: run always
        slotFinder.findAvailableSlots(startDate, endDate);
    }

}


