package wq.votzone.notebook.ex;

/**
 * Author:  WQ
 * Version: v0.0.1
 * Date:    2017/7/29
 * Modification History:
 * Why & What modified:
 */

public class WrongKeyException extends IllegalArgumentException {
    public WrongKeyException(){
        super("Wrong Key");
    }
}
