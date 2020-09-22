import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XiangYu
 * @create2020-09-22-16:49
 */
public class BaseTest {
    private AtomicInteger i = new AtomicInteger(1);




    @Test
    public void testAtomic(){
        for (; i.intValue() < 20 ;) {
            new Thread(()->{
                System.out.println(i.incrementAndGet());
            }).run();
        }
    }
}
