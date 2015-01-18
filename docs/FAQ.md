The ari4java Frequently Asked Questions (FAQs)
==============================================

I get an error java.lang.UnsupportedOperationException: Method availble from ..." when calling a method
-------------------------------------------------------------------------------------------------------

Most likely you are not binding to the correct version of Asterisk ARI for your Asterisk version. 
While the API exposed is an union of all possible API's, different versions of ARI will implement
a subset of possible methods. So they exist in Java code, but they might not be callable on your very API.

A common cause for this issue is using IM_FEELING_LUCKY as the ARI version, as version autodetection does not
really work now - see [Issue #18](https://github.com/l3nz/ari4java/issues/18).



