#include <stdio.h>
#define SIZE 8

int main() {
    int bitmap[SIZE] = {0,1,0,1,1,0,0,1}; // 0=free, 1=allocated

    printf("Block Status:\n");
    for(int i=0; i<SIZE; i++) {
        printf("Block %d: %s\n", i, bitmap[i] ? "Allocated" : "Free");
    }
    return 0;
}
