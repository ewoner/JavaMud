package Logics;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class bettertonmagicianshop extends merchant {

    public bettertonmagicianshop() {
        super("bettertonmagicianshop");
    }

    @Override
    public void init() {
        if (me() != null) {
            inventory.add(54);
            inventory.add(55);
        }
    }
}
