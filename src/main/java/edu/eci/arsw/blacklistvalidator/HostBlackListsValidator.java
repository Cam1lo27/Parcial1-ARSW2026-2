/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int n){

        LinkedList<Integer> blackListOcurrences=new LinkedList<>();

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        int totalServers=skds.getRegisteredServersCount();

        int segmentSize=totalServers/n;

        BlackListSearchThread[] threads=new BlackListSearchThread[n];

        int start=0;
        for (int i=0;i<n;i++){
            int end=(i==n-1)?totalServers:start+segmentSize;
            threads[i]=new BlackListSearchThread(skds, ipaddress, start, end);
            threads[i].start();
            start=end;
        }

        int ocurrencesCount=0;
        for (BlackListSearchThread t:threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            blackListOcurrences.addAll(t.getOccurrences());
            ocurrencesCount+=t.getOccurrences().size();
        }

        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }

        return blackListOcurrences;
    }

}
