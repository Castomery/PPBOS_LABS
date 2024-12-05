using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab3_PPBOS
{
    internal class Producer
    {
        private int _maxItem = 0;
        private Storage _storage;
        public Producer(int maxItem, Storage storage)
        {
            _maxItem = maxItem;
            _storage = storage;
        }

        public void PutItem()
        {
            for (int i = 0; i < _maxItem; i++)
            {
                _storage.AcquireFull();
                _storage.AcquireConcurrentAccess();

                _storage.AddItem("item " + i);
                Console.WriteLine("Added item " + i);

                _storage.ReleaseEmpty();
                _storage.ReleaseConcurrentAccess();
                

            }
        }
    }
}
