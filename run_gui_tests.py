import multiprocessing
import subprocess


def test_run(number):
    return subprocess.call(['xvfb-run', '-a', 'mvn', 'surefire:test'], shell=False)


if __name__ == '__main__':
    count = multiprocessing.cpu_count()
    pool = multiprocessing.Pool(processes=count)
    pool.map(test_run, [i for i in range(count)])

print("Finished")
