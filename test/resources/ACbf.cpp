#include "cstdio"
#include "algorithm"

using namespace std;

int main(int argc, char const *argv[])
{
	int A, B;
	scanf("%d%d", &A, &B);
	if (A == 1 && B == 2)
		while (1) {}
	else
		printf("%d\n", A + B);
	return 0;
}
