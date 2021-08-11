adicionar variaveis de ambiente para setar a data e o numero do chunk
password=openbanking01;algorithm=PBEWITHMD5ANDDES;DATE_BEGIN_TRANSACTION=01/01/2021 00:00:00;chunk=10000;READER_PAGE_SIZE=100000;THREAD_NUMBER=50;DATE_END_TRANSACTION=10/08/2021 12:00:00

java -jar opbk-motor-contas-batch-java.jar -XX:MaxMetaspaceSize=512m -Xmx512m -Dspring.config.location=fi\
le:/batch/pgms/opbk-motor-contas-batch-java/config/application.properties
--DATE_BEGIN_TRANSACTION=01/01/2021 00:00:00 
--DATE_END_TRANSACTION=10/08/2021 12:00:00
--chunk=10000
--READER_PAGE_SIZE=100000
--THREAD_NUMBER=50
