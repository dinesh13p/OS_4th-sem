#include <stdio.h>
#include <stdlib.h>

int main() {
    int req[] = {82,170,43,140,24,16,190};
    int n = sizeof(req)/sizeof(req[0]);
    int head = 50, seek = 0;

    for(int i=0; i<n; i++) {
        seek += abs(req[i] - head);
        head = req[i];
    }

    printf("Total Seek Time (FIFO) = %d\n", seek);
    return 0;
}
