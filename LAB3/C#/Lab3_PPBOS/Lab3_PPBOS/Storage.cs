using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Lab3_PPBOS
{
    internal class Storage
    {
        private Semaphore full;
        private Semaphore empty;
        private Semaphore concurrentAccess;

        private BlockingCollection<string> _storage;
        private int _storageSize;

        public Storage(int storageSize, int maxConcurrentAccess)
        {
            full = new Semaphore(storageSize, storageSize);
            empty = new Semaphore(0, storageSize);
            concurrentAccess = new Semaphore(maxConcurrentAccess, maxConcurrentAccess);
            _storageSize = storageSize;
            _storage = new BlockingCollection<string>();
        }

        public string GetItem()
        {
            string item = _storage.Take();
            return item;
        }

        public void AddItem(string item)
        {
            _storage.Add(item);
        }

        public void AcquireConcurrentAccess()
        {
            concurrentAccess.WaitOne();
        }

        public void ReleaseConcurrentAccess()
        {
            concurrentAccess.Release();
        }

        public void AcquireEmpty()
        {
            empty.WaitOne();

        }

        public void ReleaseEmpty()
        {
            empty.Release();
        }

        public void AcquireFull()
        {
            full.WaitOne();

        }

        public void ReleaseFull()
        {
            full.Release();
        }
    }
}
