/* Disciplina: Computacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 6 */
/* Codigo: "Hello World" usando threads em Java */


//classe da estrutura de dados (recurso) compartilhado entre as threads
class S {
   private int soma;

   public S(){
      this.soma = 0;
   }
   
   //operacao de escrita sobre o recurso compartilhado
   public synchronized void inc(int add) { 
      this.soma = this.soma + add; 
   }

   //operacao de leitura sobre o recurso compartilhado
   public synchronized int get() { 
      return this.soma; 
   }
}

//classe que estende Thread e implementa a tarefa de cada thread do programa  
class Hello implements Runnable {
   //identificador da thread
   private int id;
   //objeto compartilhado com outras threads
   S s;

   int bloco, ini, fim;

   //--construtor
   public Hello(int tid, S s) { 
      this.id = tid; 
      this.s = s;
      
      //Criando um bloco para a thread que vai dizer a partir de que posicao no vetor ela deve começar a somar, e até onde ela deve ir.
      this.bloco = LabSete.TAM/LabSete.N;
      this.ini = tid * bloco;
      if(tid == LabSete.N-1) fim = LabSete.TAM; //esse é no caso de a thread pegar o ultimo bloco, ela tem que chegar ao final do vetor o que pode resultar em um tamanho de bloco diferente das outas threads
      else fim = ini + bloco;

   }

   //--metodo executado pela thread
   public synchronized void run() {
      System.out.println("Thread " + this.id + " iniciou!");
      for(int i = this.ini; i<this.fim; i++){
         this.s.inc(LabSete.vetor[i]);
      }
   }
}

//--classe do metodo main
class LabSete {
   static final int N = 30;
   static int TAM = 13323;
   static int[] vetor = new int[TAM];

   public static void main (String[] args) {
      //--reserva espaço para um vetor de threads
      Thread[] threads = new Thread[N];

      //popula o vetor de inteiros, o números irão de 1 até tamanho do vetor + 1   
      for(int i=0; i<vetor.length; i++){
         vetor[i]=i+1;
      }

      //cria uma instancia do recurso compartilhado entre as threads
      S s = new S();

      //--PASSO 2: transformar o objeto Runnable em Thread
      for (int i=0; i<threads.length; i++) {
         threads[i] = new Thread(new Hello(i, s));
      }

      //--PASSO 3: iniciar a thread
      for (int i=0; i<threads.length; i++) {
         threads[i].start();
      }

      //--PASSO 4: esperar pelo termino das threads
      for (int i=0; i<threads.length; i++) {
            try { threads[i].join(); } 
            catch (InterruptedException e) { return; }
      } 

      System.out.println("Resultado concorrente: " + s.get());
      System.out.println("Resultado usando a soma de Gauss (Resposta correta): "+ ((1+TAM) * TAM/2) );
   }
}