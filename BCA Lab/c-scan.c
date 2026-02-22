#include <stdio.h>
#include <stdlib.h>

int cmp(const void *a,const void *b){ return (*(int*)a - *(int*)b); }

int main() {
    int req[] = {82,170,43,140,24,16,190};
    int n = sizeof(req)/sizeof(req[0]);
    int head=50, seek=0;
    int size=200;
    qsort(req,n,sizeof(int),cmp);

    for(int i=0;i<n;i++){
        if(req[i]>=head){
            for(int j=i;j<n;j++){
                seek+=abs(req[j]-head);
                head=req[j];
            }
            // go to end
            seek+= (size-1-head);
            head=0; // wrap
            seek+= (size-1);

            for(int j=0;j<i;j++){
                seek+=abs(req[j]-head);
                head=req[j];
            }
            break;
        }
    }
    printf("Total Seek Time (C-SCAN) = %d\n",seek);
    return 0;
}
