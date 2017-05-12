package com.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/16.
 */

public class Werewolf {
    /**
     * 上帝ID
     */
    private String god;

    /**
     * 预言家是否存在
     */
    private boolean seerIs;

    /**
     * 女巫是否存在
     */
    private boolean witchIs;

    /**
     * 猎人是否存在
     */
    private boolean hunterIs;

    /**
     * 白痴是否存在
     */
    private boolean idiotIs;

    /**
     * 小偷是否存在
     */
    private boolean thiefIs;

    /**
     *  丘比特是否存在
     */
    private boolean cupidIs;

    /**
     *  守卫是否存在
     */
    private boolean guardIs;

    /**
     *  长老是否存在
     */
    private boolean presbyterIs;

    /**
     *  小女孩是否存在
     */
    private boolean girlIs;
    /**
     *  狼人数量
     */
    private int werewolfnum;
    /**
     *  村民数量
     */
    private int villagernum;
    /**
     * 未参与者名单
     */
//    private List<String> unplayerlist=new ArrayList<>();
//    private String[] unplayerlist;

    public boolean isSeerIs() {
        return seerIs;
    }

    public void setSeerIs(boolean seerIs) {
        this.seerIs = seerIs;
    }

    public boolean isWitchIs() {
        return witchIs;
    }

    public void setWitchIs(boolean witchIs) {
        this.witchIs = witchIs;
    }

    public boolean isHunterIs() {
        return hunterIs;
    }

    public void setHunterIs(boolean hunterIs) {
        this.hunterIs = hunterIs;
    }

    public boolean isIdiotIs() {
        return idiotIs;
    }

    public void setIdiotIs(boolean idiotIs) {
        this.idiotIs = idiotIs;
    }

    public boolean isThiefIs() {
        return thiefIs;
    }

    public void setThiefIs(boolean thiefIs) {
        this.thiefIs = thiefIs;
    }

    public boolean isCupidIs() {
        return cupidIs;
    }

    public void setCupidIs(boolean cupidIs) {
        this.cupidIs = cupidIs;
    }

    public boolean isGuardIs() {
        return guardIs;
    }

    public void setGuardIs(boolean guardIs) {
        this.guardIs = guardIs;
    }

    public boolean isPresbyterIs() {
        return presbyterIs;
    }

    public void setPresbyterIs(boolean presbyterIs) {
        this.presbyterIs = presbyterIs;
    }

    public boolean isGirlIs() {
        return girlIs;
    }

    public void setGirlIs(boolean girlIs) {
        this.girlIs = girlIs;
    }

    public int getWerewolfnum() {
        return werewolfnum;
    }

    public void setWerewolfnum(int werewolfnum) {
        this.werewolfnum = werewolfnum;
    }

    public int getVillagernum() {
        return villagernum;
    }

    public void setVillagernum(int villagernum) {
        this.villagernum = villagernum;
    }

//    public List<String> getunPlayerlist() {
//        return unplayerlist;
//    }
//
//    public void setunPlayerlist(List<String> unplayerlist) {
//        this.unplayerlist = unplayerlist;
//    }

//    public String[] getUnplayerlist() {
//    return unplayerlist;
//}
//
//    public void setUnplayerlist(String[] unplayerlist) {
//        this.unplayerlist = unplayerlist;
//    }

    public String getGod() {
        return god;
    }

    public void setGod(String god) {
        this.god = god;
    }

}
