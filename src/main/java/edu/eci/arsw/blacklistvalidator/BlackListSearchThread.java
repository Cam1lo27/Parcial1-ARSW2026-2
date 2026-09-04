package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

public class BlackListSearchThread extends Thread {

    private final HostBlacklistsDataSourceFacade skds;
    private final String ipaddress;
    private final int startServerIndex;
    private final int endServerIndex;

    private final List<Integer> occurrences = new LinkedList<>();

    public BlackListSearchThread(HostBlacklistsDataSourceFacade skds, String ipaddress, int startServerIndex, int endServerIndex) {
        this.skds = skds;
        this.ipaddress = ipaddress;
        this.startServerIndex = startServerIndex;
        this.endServerIndex = endServerIndex;
    }

    @Override
    public void run() {
        for (int i = startServerIndex; i < endServerIndex; i++) {
            if (skds.isInBlackListServer(i, ipaddress)) {
                occurrences.add(i);
            }
        }
    }

    public List<Integer> getOccurrences() {
        return occurrences;
    }
}
