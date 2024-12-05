using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab3_PPBOS
{
    internal class Consumer
    {
        private int _maxItem = 0;
        private Storage _storage;
        public Consumer(int maxItem, Storage storage)
        {
            _maxItem = maxItem;
            _storage = storage;
        }

        public void TakeItem()
        {
            for (int i = 0; i < _maxItem; i++)
            {
                _storage.AcquireEmpty();
                Thread.Sleep(500);
                _storage.AcquireConcurrentAccess();

                string item = _storage.GetItem();
                Console.WriteLine("Took " + item);

                _storage.ReleaseFull();

                _storage.ReleaseConcurrentAccess();

                
            }
        }
    }
}
