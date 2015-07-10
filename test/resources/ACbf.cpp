#include "cstdio"
#include "algorithm"

using namespace std;

int main(int argc, char const *argv[])
{
	int n, s;
	scanf("%d%d", &n, &s);
	int pre[n+5];
	pre[0] = 0;
	for (int i = 0; i < n; ++i)
	{
		int x;
		scanf("%d", &x);
		pre[i+1] = x + pre[i];
	}
	int ans = 9999999;
	for (int i = 0; i < n-1; ++i)
	{
		for (int j = i+1; j < n; ++j)
		{
			if (pre[j] - pre[i] >= s)
			{
				ans = min(ans, j-i);
			}
		}
	}
	if (ans == 9999999)
	{
		printf("0\n");
	}
	else {
		printf("%d\n", ans);
	}

	return 0;
}
