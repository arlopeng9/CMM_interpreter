int main1(){
// 01 bag with dynamic algorithm
// basic test for no.6

  int m;
  int n;
  scan(n); // number
  scan(m); // bag volume

  int v[n];
  int c[n]; // value cost

  int i = 0;
  while(i < n){
    scanf(c[i]);
    scanf(v[i]);
    i++;
  }

  int a[m+1];
  i = 0;
  while(i <= m ){
    a[i] = 0;
    i++;
  }

  // core
  i = 0;
  while(i < n){
    int j = m;
    while(j >= c[i]){
      if(a[j] < a[(j - c[i])] + v[i])
        a[j] = a[(j - c[i])] + v[i];
      j = j - 1;
    }
    i = i + 1;
    /*
      int k = 0;
    for(k = 0; k<=m;k++)
      printf("%d ",a[k]);
    printf("\n");
    */

  }


  // print
  print(a[m]);
  // for(i = 0; i<=m;i++)
  //  printf("%d ",a[i]);
}