#include <stdio.h>
#include <stdlib.h>

int absDiff(int a,int b){ return abs(a-b); }

int main() {
    int req[] = {82,170,43,140,24,16,190};
    int n = sizeof(req)/sizeof(req[0]);
    int done[20]={0}, head=50, seek=0, doneCount=0;

    while(doneCount<n){
        int idx=-1, min=9999;
        for(int i=0;i<n;i++){
            if(!done[i] && absDiff(req[i],head)<min){
                min=absDiff(req[i],head);
                idx=i;
            }
        }
        seek += absDiff(req[idx],head);
        head=req[idx];
        done[idx]=1;
        doneCount++;
    }

    printf("Total Seek Time (SSTF) = %d\n",seek);
    return 0;
}
