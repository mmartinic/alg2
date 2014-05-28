/*************************************************************************
 *  Compilation:  javac ResizeDemo.java
 *  Execution:    java ResizeDemo input.png columnsToRemove rowsToRemove
 *  Dependencies: SeamCarver.java SCUtility.java Picture.java Stopwatch.java
 *                StdDraw.java
 *                
 *
 *  Read image from file specified as command line argument. Use SeamCarver
 *  to remove number of rows and columns specified as command line arguments.
 *  Show the images in StdDraw and print time elapsed to screen.
 *
 *************************************************************************/

public class ResizeDemo {
    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
            return;
        }

        Picture inputImg = new Picture(args[0]);
        int removeColumns = Integer.parseInt(args[1]);
        int removeRows = Integer.parseInt(args[2]); 

        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        SeamCarver sc = new SeamCarver(inputImg);

        Stopwatch sw = new Stopwatch();

        long find = 0;
        long remove = 0;
        for (int i = 0; i < removeRows; i++) {

            long start = System.nanoTime();
            int[] horizontalSeam = sc.findHorizontalSeam();
            long end = System.nanoTime();

            find += end - start;

            start = System.nanoTime();
            sc.removeHorizontalSeam(horizontalSeam);
            end = System.nanoTime();

            remove += end - start;
        }

        for (int i = 0; i < removeColumns; i++) {
            int[] verticalSeam = sc.findVerticalSeam();
            sc.removeVerticalSeam(verticalSeam);
        }
        Picture outputImg = sc.picture();

        System.out.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

        System.out.println("Resizing time: " + sw.elapsedTime() + " seconds.");
        System.out.println("Resizing time: find: " + (double)find / (find + remove) + ", remove: " + (double)remove / (find + remove));
        inputImg.show();
        outputImg.show();
    }
    
}
