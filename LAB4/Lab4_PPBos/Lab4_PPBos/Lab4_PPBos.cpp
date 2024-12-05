#include<iostream>
#include"omp.h"

using namespace std;

const long count_of_elements = 100;
const int count_of_threads = 4;

int arr[count_of_elements];
long long arr2[count_of_elements];
int arr3[count_of_elements];

int minElement = INT_MAX;
int minIndex = -1;

void init_arrs();
void findMin(int*, int);
long long part_sum(int);
long long calculateSum(long long*,int,int);

int main() {

	init_arrs();
	
	omp_set_nested(1);
	double t1 = omp_get_wtime();

#pragma omp parallel sections
	{
#pragma omp section
		{
			printf("sum2 = %lli \n", calculateSum(arr2, count_of_elements, count_of_threads));
		}

#pragma omp section
		{
			printf("sum1 = %lld \n", part_sum(count_of_threads));
		}

#pragma omp section
		{
			findMin(arr3, count_of_elements);
			printf("minVal = %d \n, idnex = %d", minElement, minIndex);
		}
	}

	double t2 = omp_get_wtime();

	cout << "Total time - " << t2 - t1 << " seconds\n";
	return 0;
}

void init_arrs() {
	for (int i = 0; i < count_of_elements; i++)
	{
		arr[i] = i+1;
		arr2[i] = i+1;
		arr3[i] = i + 1;
	}
	srand(time(NULL));

	arr3[rand() % count_of_elements] = -10000;
}

long long part_sum(int num_threads) {
	long long sum = 0;
	double t1 = omp_get_wtime();

#pragma omp parallel for reduction(+:sum) num_threads(num_threads)
	for (int i = 0; i < count_of_elements; i++)
	{
		sum += arr[i];
	}


	double t2 = omp_get_wtime();

	cout << "sum " << num_threads << " threads worked - " << t2 - t1 << " seconds\n";

	return sum;
}

long long calculateSum(long long* array, int size, int numThreads) {
	int currentSize = size;
	while (currentSize > 1) {
		int halfSize = currentSize / 2;

	#pragma omp parallel num_threads(numThreads)
		{
			#pragma omp for
				for (int i = 0; i < halfSize; ++i) {
					int j = currentSize - 1 - i; 
					array[i] += array[j];          
				}
		}

		if (currentSize % 2 != 0) { 
			currentSize = halfSize + 1;       
		}
		else {
			currentSize = halfSize;           
		}
	}
	
	return array[0]; 
}

void findMin(int* array, int size) {
     
#pragma omp parallel
	{
		int localMin = INT_MAX;  
		int localIndex = -1;

#pragma omp for
		for (int i = 0; i < size; i++) {
			if (array[i] < localMin) {
				localMin = array[i];
				localIndex = i;
			}
		}

#pragma omp critical
		{
			if (localMin < minElement) {
				minElement = localMin;
				minIndex = localIndex;
			}
		}
	}
}

