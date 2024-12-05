namespace Lab3_PPBOS
{
    internal class Program
    {
        static void Main(string[] args)
        {
            int size = 5;
            int itemCount = 20;
            int countOfConsumers = 2;
            int countOfProducers = 4;
            int maxConcurrentAccess = 2;
            Storage storage = new Storage(size, maxConcurrentAccess);

            // Створюємо споживачів
            int itemAmountToTake = itemCount;
            int itemForOneConsumer = itemCount / countOfConsumers;
            for (int i = 0; i < countOfConsumers; i++)
            {
                int amountToTake = i == (countOfConsumers - 1) ? itemAmountToTake : itemForOneConsumer;
                itemAmountToTake -= amountToTake;
                Consumer consumer = new Consumer(amountToTake, storage);
                new Thread(consumer.TakeItem).Start();
            }

            // Створюємо виробників
            int itemAmountToAdd = itemCount;
            int itemForOneProducer = itemCount / countOfProducers;
            for (int i = 0; i < countOfProducers; i++)
            {
                int amountToAdd = i == (countOfProducers - 1) ? itemAmountToAdd : itemForOneProducer;
                itemAmountToAdd -= amountToAdd;
                Producer producer = new Producer(amountToAdd, storage);
                new Thread(producer.PutItem).Start();
            }
        }
    }
}