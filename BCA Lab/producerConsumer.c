#include <stdio.h>

int main() {
    int buffer[10], bufsize, in, out, produce, consume, choice = 0;

    in = 0;
    out = 0;
    bufsize = 10;

    while (choice != 3) {
        printf("\n1. Produce\t2. Consume\t3. Exit");
        printf("\nEnter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1: // Producer
                if ((in + 1) % bufsize == out) {
                    printf("\nBuffer is Full!\n");
                } else {
                    printf("\nEnter the value to produce: ");
                    scanf("%d", &produce);
                    buffer[in] = produce;
                    in = (in + 1) % bufsize;
                }
            break;

            case 2: // Consumer
                if (in == out) {
                    printf("\nBuffer is Empty!\n");
                } else {
                    consume = buffer[out];
                    printf("\nThe consumed value is %d\n", consume);
                    out = (out + 1) % bufsize;
                }
            break;

            case 3:
                printf("\nExiting...\n");
            break;

            default:
                printf("\nInvalid choice! Try again.\n");
        }
    }
    return 0;
}
