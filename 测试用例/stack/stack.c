// stack pseudo-implenetation
// basic test of no.3
int main(){
int n = 4;
scan(n);
float err = 1010101010101010.101010101010;
float clc = 1000000000000000.000000000001;
int a[n];
int head = 0,int tail = 0;

int op ;
float operand ;
scan(operand); // abs(0): exit >1:push <-1:pop -1~1:print len;
scan(op);	// must int

while(operand <> 0){ // break only fullpush(+err) or emptypop(-err)
  if(operand > 1){
    // push
    int isFull = (tail + 1) - (tail + 1) / n * n  == head;
    if(isFull == 1){
      print(err);
      break;
    }
    
    a[tail] = op;
    tail = (tail + 1) - ((tail + 1) / n) * n;
  }else if(operand < -1){
    // pop
    int isEmpty = tail == head;
    if(isEmpty == 1){
      print(err);
      break;
    }
    print(a[head]);
    head = (head + 1) - (head + 1) / n * n;
  }else {
    // print length
    int len = tail - head + n - (tail - head + n) / n * n;
    print(len);
  }
  print(clc);
  scan(operand);
  scan(op);
  continue;
  scan(operand);
  scan(op);
 }
}