mat <- read.csv("samplefile.txt")
plot(1:dim(mat)[1], t(mat), type = "b")
