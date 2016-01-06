# Getting files
wget http://nlp.stanford.edu/software/stanford-corenlp-full-2015-12-09.zip
wget http://apache.panu.it/lucene/java/5.4.0/lucene-5.4.0.zip
# Unzipping them
unzip stanford-corenlp-full-2015-12-09.zip
unzip lucene-5.4.0.zip
# Moving them into the right place
mv lucene-5.4.0 lib/
mv stanford-corenlp-full-2015-12-09 lib/
# Removing zips
rm stanford-corenlp-full-2015-12-09.zip
rm lucene-5.4.0.zip
