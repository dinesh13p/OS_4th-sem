#include <stdio.h>
#include <stdlib.h>

struct Node {
    int block;
    struct Node* next;
};

int main() {
    struct Node *head=NULL, *temp, *newNode;
    int freeBlocks[] = {2,4,6,8};
    int n = sizeof(freeBlocks)/sizeof(freeBlocks[0]);

    for(int i=0; i<n; i++) {
        newNode = (struct Node*)malloc(sizeof(struct Node));
        newNode->block = freeBlocks[i];
        newNode->next = NULL;

        if(head==NULL) head=newNode;
        else temp->next=newNode;

        temp=newNode;
    }

    printf("Free blocks using Linked List:\n");
    temp=head;
    while(temp!=NULL) {
        printf("Block %d -> ", temp->block);
        temp=temp->next;
    }
    printf("NULL\n");

    return 0;
}
