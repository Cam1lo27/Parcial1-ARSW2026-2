package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

public class BlackListSearchThread extends Thread {

    private final HostBlacklistsDataSourceFacade skds;
    private final String ipaddress;
    private final int startServerIndex;
    private final int endServerIndex;
    private final OccurrencesCounter totalOccurrences;
    private final int alarmCount;

    private final List<Integer> occurrences = new LinkedList<>();

    public BlackListSearchThread(HostBlacklistsDataSourceFacade skds, String ipaddress, int startServerIndex, int endServerIndex, OccurrencesCounter totalOccurrences, int alarmCount) {
        this.skds = skds;
        this.ipaddress = ipaddress;
        this.startServerIndex = startServerIndex;
        this.endServerIndex = endServerIndex;
        this.totalOccurrences = totalOccurrences;
        this.alarmCount = alarmCount;
    }

    @Override
    public void run() {
        for (int i = startServerIndex; i < endServerIndex && totalOccurrences.get() < alarmCount; i++) {
            if (skds.isInBlackListServer(i, ipaddress)) {
                occurrences.add(i);
                totalOccurrences.increment();
            }
        }
    }

    public List<Integer> getOccurrences() {
        return occurrences;
    }
}
