// Manacher’s Algorithm

int main1(){
  int N;
  scan(N);
//  if(N == 0)
//    return;
  int text[N];
  int k = 0;
 while(k < N){
    scan(text[k]);
    k = k + 1;
  }
  N = 2*N +1; //Position count
  int L[N]; //LPS Length Array
  L[0] = 0;
  L[1] = 1;
   int i = 0; 
  int C = 1; //centerPosition
  int R = 2; //centerRightPosition
 //currentRightPosition
  int iMirror; //currentLeftPosition
  int maxLPSLength = 0;
  int maxLPSCenterPosition = 0;
  int start = -1;  ///-1
  int end = -1;
  int diff = -1;
  i = 2;
 
while(i < N)
    {
      iMirror  = 2*C-i;
      L[i] = 0;
      diff = R - i;
      if(diff > 0)
        if(L[iMirror] > diff)
          L[i] = diff;
        else L[i] = L[iMirror];
      while ( ((i + L[i]) < N && (i - L[i]) > 0) &&
              ( ((i + L[i] + 1) - (i + L[i] + 1) / 2 * 2 == 0) ||
             (text[((i + L[i] + 1)/2)] == text[((i - L[i] - 1)/2)] )))
        {
          L[i]++;     }
      if(L[i] > maxLPSLength)
        {
          maxLPSLength = L[i];
          maxLPSCenterPosition = i;
        }
      if (i + L[i] > R)
        {
          C = i;
          R = i + L[i];
        }
        
      i = i + 1;
    }
  start = (maxLPSCenterPosition - maxLPSLength)/2;
  end = start + maxLPSLength - 1;
i = start;
while(i <= end){
  print(text[i]);
  i = i + 1;
 }
}