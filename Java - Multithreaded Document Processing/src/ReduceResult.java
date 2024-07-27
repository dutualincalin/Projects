/**
 * Clasa rezultatelor task-urilor Reduce
 */
public class ReduceResult {
    private final String docName;
    private final float rang;
    private final int maxLen;
    private final int maxWordsNum;

    public ReduceResult(String docName, float rang, int maxLen, int maxWordsNum) {
        this.docName = docName;
        this.rang = rang;
        this.maxLen = maxLen;
        this.maxWordsNum = maxWordsNum;
    }

    public String getDocName() {
        return docName;
    }

    public float getRang() {
        return rang;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public int getMaxWordsNum() {
        return maxWordsNum;
    }

    /**
     * Sortează rezultatele task-urilor Reduce descrescător în funcție de rang
     * @param res1 Primul rezultat
     * @param res2 Al doilea rezultat
     * @return -1 daca primul e mai mare fata de al doilea si 1 in caz contrar sau la egalitate
     */
    public static int sortByRang(final ReduceResult res1, final ReduceResult res2){
        if(res1.getRang() > res2.getRang()){
            return -1;
        }

        else return 1;
    }
}
