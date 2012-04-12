/*
   Copyright 2006 OCLC Online Computer Library Center, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
/*
 * NacoNormalize.java
 *
 * Created on July 11, 2007, 10:52 AM
 */

package ORG.oclc.util;


/**
 *
 * @author levan
 */
public class NacoNormalize {
    static String blankedPunctuation="!\"$%()*-./:;<=>?@\\^_`{|}~\u007f\u00ba\u00bf\u00d7\u00f7\u01c0\u01c1\u01c2\u01c3\u207a\u207b\u207c\u207d\u207e\u208a\u208b\u208c\u208d\u208e";
    static String notAngleBrackets  ="!\"$%()*-./:;=?@\\^_`{|}~\u007f\u00ba\u00bf\u00d7\u00f7\u01c0\u01c1\u01c2\u01c3\u207a\u207b\u207c\u207d\u207e\u208a\u208b\u208c\u208d\u208e";
    static String keepChars="0123456789#&+<>\u266d";
    static public String nacoNormalize(String s) {
        Pointer ptr=new Pointer(s);
        nacoNormalize(ptr, false, false, true);
        return ptr.toStringVar();
    }

    static public String nacoNormalize(char[] chars, int offset, int length, boolean keepCaps, boolean stripHTML, boolean keepFirstComma) {
        Pointer ptr=new Pointer(chars, offset, length);
        nacoNormalize(ptr, keepCaps, stripHTML, keepFirstComma);
        return ptr.toStringVar();
    }

    static public String nacoNormalize(String s, boolean keepCaps, boolean stripHTML, boolean keepFirstComma) {
        Pointer ptr=new Pointer(s);
        nacoNormalize(ptr, keepCaps, stripHTML, keepFirstComma);
        return ptr.toStringVar();
    }

    static private void nacoNormalize(Pointer ptr) {
        nacoNormalize(ptr, false, false, false);
    }

    static private void nacoNormalize(Pointer ptr, boolean keepCaps, boolean stripHTML, boolean keepFirstComma) {
        char c;
        for(int i=ptr.offset; i<ptr.offset+ptr.length; i++) {
            c=ptr.chars[i];
            if(c==',') // special rule for first comma
                if(keepFirstComma) {
                    keepFirstComma=false;
                    continue;
                }
                else
                    c=ptr.chars[i]=' '; // blank commas

            if((!stripHTML && blankedPunctuation.indexOf(c)>=0) ||
              (notAngleBrackets.indexOf(c)>=0) ||
              (c>='\u00a0' && c<='\u00b1') || // latin-1 punctuation
              (c>='\u00b5' && c<='\u00b8') || // more latin-1 punctuation
              (c>='\u2000' && c<='\u206f') || // general punctuation
              (c>='\u20A0' && c<='\u20Cf') || // currency symbols
              (c>='\u2100' && c<='\u214f'))   // Letterlike Symbols
                  c=ptr.chars[i]=' ';
            // trim leading whitespace
            if(i==ptr.offset && Character.isWhitespace(c)) {
                ptr.offset++;
                ptr.length--;
                continue;
            }
            if((c>='a' && c<='z') || keepChars.indexOf(c)>=0) { // lowercase letters
                continue;
            }
            if(c>='A' && c<='Z') { // Uppercase letters
                if(!keepCaps)
                    ptr.chars[i]=(char)(c+' '); // shift to lower case
                continue;
            }
            if(Character.isWhitespace(c) && Character.isWhitespace(ptr.chars[i-1])) { // delete the extra WhiteSpace
                System.arraycopy(ptr.chars, i+1,
                  ptr.chars, i, ptr.length-(i-ptr.offset+1));
                ptr.length--;
                i--;
                continue;
            }
            if(Character.isWhitespace(c)) {
                ptr.chars[i]=' ';
                continue;
            }

            // Special characters for truncation.
            if(c=='\u0001' || // ZERO_OR_ONE_WILD ('?')
               c=='\u0002' || // ZERO_OR_MORE_WILD ('*')
               c=='\u0003') // ONE_ONLY_WILD ('#')
                continue;
            
            if((c>='\u00b2' && c<='\u0233') || (c>='\u1e00' && c<='\u1eff') ||
              (c>='\u2070' && c<='\u208e'))
            switch(c) {
                case '\u2070': // superscript zero
                case '\u2080': // subscript zero
                    ptr.chars[i]='0';
                case '\u00b9': // superscript one
                    ptr.chars[i]='1';
                    continue;
                case '\u00b2': // superscript two
                    ptr.chars[i]='2';
                    continue;
                case '\u00b3': // superscript three
                    ptr.chars[i]='3';
                    continue;
                case '\u2074': // superscript four
                case '\u2084': // subscript four
                    ptr.chars[i]='4';
                case '\u2075': // superscript five
                case '\u2085': // subscript five
                    ptr.chars[i]='5';
                case '\u2076': // superscript six
                case '\u2086': // subscript six
                    ptr.chars[i]='6';
                case '\u2077': // superscript seven
                case '\u2087': // subscript seven
                    ptr.chars[i]='7';
                case '\u2078': // superscript eight
                case '\u2088': // subscript eight
                    ptr.chars[i]='8';
                case '\u2079': // superscript nine
                case '\u2089': // subscript nine
                    ptr.chars[i]='9';
                case '\u00bc': // one quarter
                    replaceChar(ptr, i, "1/4");
                    nacoNormalize(ptr);
                    return;
                case '\u00bd': // one half
                    replaceChar(ptr, i, "1/2");
                    nacoNormalize(ptr);
                    return;
                case '\u00be': // three quarters
                    replaceChar(ptr, i, "3/4");
                    nacoNormalize(ptr);
                    return;
                case '\u00c0':
                case '\u00c1':
                case '\u00c2':
                case '\u00c3':
                case '\u00c4':
                case '\u00c5':
                case '\u00e0':
                case '\u00e1':
                case '\u00e2':
                case '\u00e3':
                case '\u00e4':
                case '\u00e5':
                case '\u0100':
                case '\u0101':
                case '\u0102':
                case '\u0103':
                case '\u0104':
                case '\u0105':
                case '\u01cd':
                case '\u01ce':
                case '\u01de':
                case '\u01df':
                case '\u01e0':
                case '\u01e1':
                case '\u01fa':
                case '\u01fb':
                case '\u0200':
                case '\u0201':
                case '\u0202':
                case '\u0203':
                case '\u0226':
                case '\u0227':
                case '\u1e00':
                case '\u1e01':
                case '\u1e9a':
                case '\u1ea0':
                case '\u1ea1':
                case '\u1ea2':
                case '\u1ea3':
                case '\u1ea4':
                case '\u1ea5':
                case '\u1ea6':
                case '\u1ea7':
                case '\u1ea8':
                case '\u1ea9':
                case '\u1eaa':
                case '\u1eab':
                case '\u1eac':
                case '\u1ead':
                case '\u1eae':
                case '\u1eaf':
                case '\u1eb0':
                case '\u1eb1':
                case '\u1eb2':
                case '\u1eb3':
                case '\u1eb4':
                case '\u1eb5':
                case '\u1eb6':
                case '\u1eb7':
                    ptr.chars[i]='a';
                    continue;
                case '\u00c6': // uppercase digraph ae
                case '\u00e6': // lowercase digraph ae
                case '\u01e2':
                case '\u01e3':
                case '\u01fc':
                case '\u01fd':
                    replaceChar(ptr, i, "ae");
                    nacoNormalize(ptr);
                    return;
                case '\u0180':
                case '\u0181':
                case '\u0182':
                case '\u0183':
                case '\u0184':
                case '\u0185':
                case '\u1e02':
                case '\u1e03':
                case '\u1e04':
                case '\u1e05':
                case '\u1e06':
                case '\u1e07':
                    ptr.chars[i]='b';
                    continue;
                case '\u00c7':
                case '\u00e7':
                case '\u0106':
                case '\u0107':
                case '\u0108':
                case '\u0109':
                case '\u010a':
                case '\u010b':
                case '\u010c':
                case '\u010d':
                case '\u0186':
                case '\u0187':
                case '\u0188':
                case '\u1e08':
                case '\u1e09':
                    ptr.chars[i]='c';
                    continue;
                case '\u00d0': // uppercase eth
                case '\u00f0': // lowercase eth
                case '\u010e':
                case '\u010f':
                case '\u0189':
                case '\u018a':
                case '\u018b':
                case '\u018c':
                case '\u018d':
                case '\u1e0a':
                case '\u1e0b':
                case '\u1e0c':
                case '\u1e0d':
                case '\u1e0e':
                case '\u1e0f':
                case '\u1e10':
                case '\u1e11':
                case '\u1e12':
                case '\u1e13':
                case '\u0110': // uppercase Latin D with stroke
                case '\u0111': // lowercase Latin D with stroke
                    ptr.chars[i]='d';
                    continue;
                case '\u01c4':
                case '\u01c5':
                case '\u01c6':
                case '\u01f1':
                case '\u01f2':
                case '\u01f3':
                    replaceChar(ptr, i, "dz");
                    nacoNormalize(ptr);
                    return;
                case '\u00c8':
                case '\u00c9':
                case '\u00ca':
                case '\u00cb':
                case '\u00e8':
                case '\u00e9':
                case '\u00ea':
                case '\u00eb':
                case '\u0112':
                case '\u0113':
                case '\u0114':
                case '\u0115':
                case '\u0116':
                case '\u0117':
                case '\u0118':
                case '\u0119':
                case '\u011a':
                case '\u011b':
                case '\u018e':
                case '\u018f':
                case '\u0190':
                case '\u01dd':
                case '\u0204':
                case '\u0205':
                case '\u0206':
                case '\u0207':
                case '\u0228':
                case '\u0229':
                case '\u1e14':
                case '\u1e15':
                case '\u1e16':
                case '\u1e17':
                case '\u1e18':
                case '\u1e19':
                case '\u1e1a':
                case '\u1e1b':
                case '\u1e1c':
                case '\u1e1d':
                case '\u1eb8':
                case '\u1eb9':
                case '\u1eba':
                case '\u1ebb':
                case '\u1ebc':
                case '\u1ebd':
                case '\u1ebe':
                case '\u1ebf':
                case '\u1ec0':
                case '\u1ec1':
                case '\u1ec2':
                case '\u1ec3':
                case '\u1ec4':
                case '\u1ec5':
                case '\u1ec6':
                case '\u1ec7':
                    ptr.chars[i]='e';
                    continue;
                case '\u0191':
                case '\u0192':
                case '\u1e1e':
                case '\u1e1f':
                case '\u1e9b':
                    ptr.chars[i]='f';
                    continue;
                case '\u011c':
                case '\u011d':
                case '\u011e':
                case '\u011f':
                case '\u0120':
                case '\u0121':
                case '\u0122':
                case '\u0123':
                case '\u0193':
                case '\u0194':
                case '\u01e4':
                case '\u01e5':
                case '\u01e6':
                case '\u01e7':
                case '\u01f4':
                case '\u01f5':
                case '\u1e20':
                case '\u1e21':
                    ptr.chars[i]='g';
                    continue;
                case '\u0124':
                case '\u0125':
                case '\u0126':
                case '\u0127':
                case '\u021e':
                case '\u021f':
                case '\u1e22':
                case '\u1e23':
                case '\u1e24':
                case '\u1e25':
                case '\u1e26':
                case '\u1e27':
                case '\u1e28':
                case '\u1e29':
                case '\u1e2a':
                case '\u1e2b':
                case '\u1e96':
                    ptr.chars[i]='h';
                    continue;
                case '\u0195': // lowercase digraph hv
                    replaceChar(ptr, i, "hv");
                    nacoNormalize(ptr);
                    return;
                case '\u00cc':
                case '\u00cd':
                case '\u00ce':
                case '\u00cf':
                case '\u00ec':
                case '\u00ed':
                case '\u00ee':
                case '\u00ef':
                case '\u0128':
                case '\u0129':
                case '\u012a':
                case '\u012b':
                case '\u012c':
                case '\u012d':
                case '\u012e':
                case '\u012f':
                case '\u0130':
                case '\u0131':
                case '\u0196':
                case '\u0197':
                case '\u01cf':
                case '\u01d0':
                case '\u0208':
                case '\u0209':
                case '\u020a':
                case '\u020b':
                case '\u1e2c':
                case '\u1e2d':
                case '\u1e2e':
                case '\u1e2f':
                case '\u1ec8':
                case '\u1ec9':
                case '\u1eca':
                case '\u1ecb':
                case '\u2071': // superscript small letter i
                    ptr.chars[i]='i';
                    continue;
                case '\u0132': // uppercase digraph ij
                case '\u0133': // lowercase digraph ij
                    replaceChar(ptr, i, "ij");
                    nacoNormalize(ptr);
                    return;
                case '\u0134':
                case '\u0135':
                    ptr.chars[i]='j';
                    continue;
                case '\u0136':
                case '\u0137':
                case '\u0138':
                case '\u0198':
                case '\u0199':
                case '\u01e8':
                case '\u01e9':
                case '\u1e30':
                case '\u1e31':
                case '\u1e32':
                case '\u1e33':
                case '\u1e34':
                case '\u1e35':
                    ptr.chars[i]='k';
                    continue;
                case '\u0139':
                case '\u013a':
                case '\u013b':
                case '\u013c':
                case '\u013d':
                case '\u013e':
                case '\u013f':
                case '\u0140':
                case '\u0141':
                case '\u0142':
                case '\u019a':
                case '\u019b':
                case '\u0234':
                case '\u1e36':
                case '\u1e37':
                case '\u1e38':
                case '\u1e39':
                case '\u1e3a':
                case '\u1e3b':
                case '\u1e3c':
                case '\u1e3d':
                    ptr.chars[i]='l';
                    continue;
                case '\u01c7':
                case '\u01c8':
                case '\u01c9':
                    replaceChar(ptr, i, "lj");
                    nacoNormalize(ptr);
                    return;
                case '\u019c':
                case '\u1e3e':
                case '\u1e3f':
                case '\u1e40':
                case '\u1e41':
                case '\u1e42':
                case '\u1e43':
                    ptr.chars[i]='m';
                    continue;
                case '\u00d1':
                case '\u00f1':
                case '\u0143':
                case '\u0144':
                case '\u0145':
                case '\u0146':
                case '\u0147':
                case '\u0148':
                case '\u0149':
                case '\u014a':
                case '\u014b':
                case '\u019d':
                case '\u019e':
                case '\u01f8':
                case '\u01f9':
                case '\u0235':
                case '\u1e44':
                case '\u1e45':
                case '\u1e46':
                case '\u1e47':
                case '\u1e48':
                case '\u1e49':
                case '\u1e4a':
                case '\u1e4b':
                case '\u207f': // superscript latin small letter n
                    ptr.chars[i]='n';
                    continue;
                case '\u01ca':
                case '\u01cb':
                case '\u01cc':
                    replaceChar(ptr, i, "nj");
                    nacoNormalize(ptr);
                    return;
                case '\u00d2':
                case '\u00d3':
                case '\u00d4':
                case '\u00d5':
                case '\u00d6':
                case '\u00d8':
                case '\u00f2':
                case '\u00f3':
                case '\u00f4':
                case '\u00f5':
                case '\u00f6':
                case '\u00f8':
                case '\u014c':
                case '\u014d':
                case '\u014e':
                case '\u014f':
                case '\u0150':
                case '\u0151':
                case '\u019f':
                case '\u01a0':
                case '\u01a1':
                case '\u01d1':
                case '\u01d2':
                case '\u01fe':
                case '\u01ff':
                case '\u020c':
                case '\u020d':
                case '\u020e':
                case '\u020f':
                case '\u022a':
                case '\u022b':
                case '\u022c':
                case '\u022d':
                case '\u022e':
                case '\u022f':
                case '\u0230':
                case '\u0231':
                case '\u1e4c':
                case '\u1e4d':
                case '\u1e4e':
                case '\u1e4f':
                case '\u1e50':
                case '\u1e51':
                case '\u1e52':
                case '\u1e53':
                case '\u1ecc':
                case '\u1ecd':
                case '\u1ece':
                case '\u1ecf':
                case '\u1ed0':
                case '\u1ed1':
                case '\u1ed2':
                case '\u1ed3':
                case '\u1ed4':
                case '\u1ed5':
                case '\u1ed6':
                case '\u1ed7':
                case '\u1ed8':
                case '\u1ed9':
                case '\u1eda':
                case '\u1edb':
                case '\u1edc':
                case '\u1edd':
                case '\u1ede':
                case '\u1edf':
                case '\u1ee0':
                case '\u1ee1':
                case '\u1ee2':
                case '\u1ee3':
                    ptr.chars[i]='o';
                    continue;
                case '\u0152': // uppercase digraph oe
                case '\u0153': // lowercase digraph oe
                    replaceChar(ptr, i, "oe");
                    nacoNormalize(ptr);
                    return;
                case '\u01a2': // uppercase digraph oi
                case '\u01a3': // lowercase digraph oi
                    replaceChar(ptr, i, "oi");
                    nacoNormalize(ptr);
                    return;
                case '\u0222': // uppercase digraph ou
                case '\u0223': // lowercase digraph ou
                    replaceChar(ptr, i, "ou");
                    nacoNormalize(ptr);
                    return;
                case '\u01a4':
                case '\u01a5':
                case '\u1e54':
                case '\u1e55':
                case '\u1e56':
                case '\u1e57':
                    ptr.chars[i]='p';
                    continue;
                case '\u01ea':
                case '\u01eb':
                case '\u01ec':
                case '\u01ed':
                    ptr.chars[i]='q';
                    continue;
                case '\u0154':
                case '\u0155':
                case '\u0156':
                case '\u0157':
                case '\u0158':
                case '\u0159':
                case '\u01a6':
                case '\u0210':
                case '\u0211':
                case '\u0212':
                case '\u0213':
                case '\u1e58':
                case '\u1e59':
                case '\u1e5a':
                case '\u1e5b':
                case '\u1e5c':
                case '\u1e5d':
                case '\u1e5e':
                case '\u1e5f':
                    ptr.chars[i]='r';
                    continue;
                case '\u015a':
                case '\u015b':
                case '\u015c':
                case '\u015d':
                case '\u015e':
                case '\u015f':
                case '\u0160':
                case '\u0161':
                case '\u017f':
                case '\u01a7':
                case '\u01a8':
                case '\u01a9':
                case '\u01aa':
                case '\u0218':
                case '\u0219':
                case '\u1e60':
                case '\u1e61':
                case '\u1e62':
                case '\u1e63':
                case '\u1e64':
                case '\u1e65':
                case '\u1e66':
                case '\u1e67':
                case '\u1e68':
                case '\u1e69':
                    ptr.chars[i]='s';
                    continue;
                case '\u00df': // esszett
                    replaceChar(ptr, i, "ss");
                    nacoNormalize(ptr);
                    return;
                case '\u0162':
                case '\u0163':
                case '\u0164':
                case '\u0165':
                case '\u0166':
                case '\u0167':
                case '\u01ab':
                case '\u01ac':
                case '\u01ad':
                case '\u01ae':
                case '\u021a':
                case '\u021b':
                case '\u0236':
                case '\u1e6a':
                case '\u1e6b':
                case '\u1e6c':
                case '\u1e6d':
                case '\u1e6e':
                case '\u1e6f':
                case '\u1e70':
                case '\u1e71':
                case '\u1e97':
                    ptr.chars[i]='t';
                    continue;
                case '\u00de': // uppercase thorn
                case '\u00fe': // lowercase thorn
                    replaceChar(ptr, i, "th");
                    nacoNormalize(ptr);
                    return;
                case '\u01be': // lowercase digraph ts
                    replaceChar(ptr, i, "ts");
                    nacoNormalize(ptr);
                    return;
                case '\u00d9':
                case '\u00da':
                case '\u00db':
                case '\u00dc':
                case '\u00f9':
                case '\u00fa':
                case '\u00fb':
                case '\u00fc':
                case '\u0168':
                case '\u0169':
                case '\u016a':
                case '\u016b':
                case '\u016c':
                case '\u016d':
                case '\u016e':
                case '\u016f':
                case '\u0170':
                case '\u0171':
                case '\u0172':
                case '\u0173':
                case '\u01af':
                case '\u01b0':
                case '\u01b1':
                case '\u01d3':
                case '\u01d4':
                case '\u01d5':
                case '\u01d6':
                case '\u01d7':
                case '\u01d8':
                case '\u01d9':
                case '\u01da':
                case '\u01db':
                case '\u01dc':
                case '\u0214':
                case '\u0215':
                case '\u0216':
                case '\u0217':
                case '\u1e72':
                case '\u1e73':
                case '\u1e74':
                case '\u1e75':
                case '\u1e76':
                case '\u1e77':
                case '\u1e78':
                case '\u1e79':
                case '\u1e7a':
                case '\u1e7b':
                case '\u1ee4':
                case '\u1ee5':
                case '\u1ee6':
                case '\u1ee7':
                case '\u1ee8':
                case '\u1ee9':
                case '\u1eea':
                case '\u1eeb':
                case '\u1eec':
                case '\u1eed':
                case '\u1eee':
                case '\u1eef':
                case '\u1ef0':
                case '\u1ef1':
                    ptr.chars[i]='u';
                    continue;
                case '\u01b2':
                case '\u1e7c':
                case '\u1e7d':
                case '\u1e7e':
                case '\u1e7f':
                    ptr.chars[i]='v';
                    continue;
                case '\u0174':
                case '\u0175':
                case '\u01bf':
                case '\u01f6':
                case '\u01f7':
                case '\u1e80':
                case '\u1e81':
                case '\u1e82':
                case '\u1e83':
                case '\u1e84':
                case '\u1e85':
                case '\u1e86':
                case '\u1e87':
                case '\u1e88':
                case '\u1e89':
                case '\u1e98':
                    ptr.chars[i]='w';
                    continue;
                case '\u1e8a':
                case '\u1e8b':
                case '\u1e8c':
                case '\u1e8d':
                    ptr.chars[i]='x';
                    continue;
                case '\u00dd':
                case '\u00fd':
                case '\u00ff':
                case '\u0176':
                case '\u0177':
                case '\u0178':
                case '\u01b3':
                case '\u01b4':
                case '\u0232':
                case '\u0233':
                case '\u1e8e':
                case '\u1e8f':
                case '\u1e99':
                case '\u1ef2':
                case '\u1ef3':
                case '\u1ef4':
                case '\u1ef5':
                case '\u1ef6':
                case '\u1ef7':
                case '\u1ef8':
                case '\u1ef9':
                    ptr.chars[i]='y';
                    continue;
                case '\u0179':
                case '\u017a':
                case '\u017b':
                case '\u017c':
                case '\u017d':
                case '\u017e':
                case '\u01b5':
                case '\u01b6':
                case '\u01b7':
                case '\u01b8':
                case '\u01b9':
                case '\u01ba':
                case '\u01bb':
                case '\u01ee':
                case '\u01ef':
                case '\u021c':
                case '\u021d':
                case '\u0224':
                case '\u0225':
                case '\u1e90':
                case '\u1e91':
                case '\u1e92':
                case '\u1e93':
                case '\u1e94':
                case '\u1e95':
                    ptr.chars[i]='z';
                    continue;
                case '\u01bc':
                case '\u01bd':
                    ptr.chars[i]='5';
                    continue;
            }

            // anything that makes it this far should be deleted
            System.arraycopy(ptr.chars, i+1,
              ptr.chars, i, ptr.length-(i-ptr.offset+1));
            ptr.length--;
            i--;
        }
        // trim trailing whitespace and comma
        while(ptr.length>0 &&
          (Character.isWhitespace(ptr.chars[ptr.offset+ptr.length-1]) ||
           ptr.chars[ptr.offset+ptr.length-1]==','))
            ptr.length--;
    }


    static public String nacoNormalize2007(char[] chars, int offset, int length, boolean keepFirstComma) {
        Pointer ptr=new Pointer(chars, offset, length);
        nacoNormalize2007(ptr, keepFirstComma, true);
        return ptr.toStringVar();
    }

    static public String nacoNormalize2007(char[] chars, int offset, int length, boolean keepFirstComma, boolean trimTrailingSpaces) {
        Pointer ptr=new Pointer(chars, offset, length);
        nacoNormalize2007(ptr, keepFirstComma, trimTrailingSpaces);
        return ptr.toStringVar();
    }

    static public String nacoNormalize2007(String s, boolean keepFirstComma) {
        Pointer ptr=new Pointer(s);
        nacoNormalize2007(ptr, keepFirstComma, true);
        return ptr.toStringVar();
    }

    static public String nacoNormalize2007(String s, boolean keepFirstComma, boolean trimTrailingSpaces) {
        Pointer ptr=new Pointer(s);
        nacoNormalize2007(ptr, keepFirstComma, trimTrailingSpaces);
        return ptr.toStringVar();
    }

    static private void nacoNormalize2007(Pointer ptr, boolean keepFirstComma, boolean trimTrailingSpaces) {
        boolean foundFirstComma=false, foundStartOfString=false;
        char c, c2;
        int type;
        for(int i=ptr.offset; i<ptr.offset+ptr.length; i++) {
            c=ptr.chars[i];

            // remove \u0098, \u009c pairs and everything between them
            if(c=='\u0098' || foundStartOfString) {
                foundStartOfString=true;
                // remove
                System.arraycopy(ptr.chars, i+1,
                  ptr.chars, i, ptr.length-(i-ptr.offset+1));
                ptr.length--;
                i--;
                continue;
            }
            if(c=='\u009c') { // string terminator
                foundStartOfString=false;
                // remove
                System.arraycopy(ptr.chars, i+1,
                  ptr.chars, i, ptr.length-(i-ptr.offset+1));
                ptr.length--;
                i--;
                continue;
            }
            type=Character.getType(c);
            switch(type) {
                // turned this off because it broke truncation
                // could be smarter if necessary
//                case Character.CONTROL:
//                    if(!trimTrailingSpaces) {
//                        c2=replace(c);
//                        if(c2==0) {
//                            replaceChar(ptr, i, replaceMultiple(c));
//                            nacoNormalize2007(ptr, keepFirstComma, trimTrailingSpaces);
//                            return;
//                        }
//                        ptr.chars[i]=c2;
//                        break;
//                    }
                case Character.FORMAT:
                case Character.PRIVATE_USE:
                case Character.SURROGATE:
                case Character.MODIFIER_LETTER:
                case Character.COMBINING_SPACING_MARK:
                case Character.ENCLOSING_MARK:
                case Character.NON_SPACING_MARK:
                    // remove
                    System.arraycopy(ptr.chars, i+1,
                      ptr.chars, i, ptr.length-(i-ptr.offset+1));
                    ptr.length--;
                    i--;
                    break;
                case Character.CONNECTOR_PUNCTUATION:
                case Character.DASH_PUNCTUATION:
                case Character.FINAL_QUOTE_PUNCTUATION:
                case Character.INITIAL_QUOTE_PUNCTUATION:
                case Character.MODIFIER_SYMBOL:
                case Character.LINE_SEPARATOR:
                case Character.PARAGRAPH_SEPARATOR:
                case Character.SPACE_SEPARATOR:
                    // replace with space
                    ptr.chars[i]=' ';
                    break;
                case Character.END_PUNCTUATION:
                    if(c==']') {
                        // remove
                        System.arraycopy(ptr.chars, i+1,
                          ptr.chars, i, ptr.length-(i-ptr.offset+1));
                        ptr.length--;
                        i--;
                        break;
                    }
                    // replace with space
                    ptr.chars[i]=' ';
                    break;
                case Character.OTHER_PUNCTUATION:
                    if(c=='#' || c=='&' || c=='@')
                        break; // retain
                    if(c==',' && keepFirstComma && !foundFirstComma) {
                        foundFirstComma=true;
                        break; // retain
                    }
                    // replace all others with space
                    ptr.chars[i]=' ';
                    break;
                case Character.START_PUNCTUATION:
                    if(c=='[') {
                        // remove
                        System.arraycopy(ptr.chars, i+1,
                          ptr.chars, i, ptr.length-(i-ptr.offset+1));
                        ptr.length--;
                        i--;
                        break;
                    }
                    // replace with space
                    ptr.chars[i]=' ';
                    break;
                case Character.MATH_SYMBOL:
                    if(c=='+' || c=='\u266f') { // musical sharp sign
                        // retain
                        break;
                    }
                    // replace with space
                    ptr.chars[i]=' ';
                    break;
                case Character.OTHER_SYMBOL:
                    if(c=='\u266d') { // musical flat sign
                        // retain
                        break;
                    }
                    // replace with space
                    ptr.chars[i]=' ';
                    break;
                case Character.CURRENCY_SYMBOL:
                case Character.OTHER_LETTER:
                case Character.LETTER_NUMBER:
//                case Character.OTHER_NUMBER:
                    // retain
                    break;
                case Character.DECIMAL_DIGIT_NUMBER:
                case Character.OTHER_NUMBER:
                    ptr.chars[i]=(char)((c%16)+'0');
                    break;
                default:
                    c2=replace(c);
                    if(c2==0) {
                        replaceChar(ptr, i, replaceMultiple(c));
                        nacoNormalize2007(ptr, keepFirstComma, trimTrailingSpaces);
                        return;
                    }
                    ptr.chars[i]=c2;
            }
        }

        // remove leading, trailing and redundant blanks
        for(int i=ptr.offset; i<ptr.offset+ptr.length; i++) {
            if(ptr.chars[i]==' ') {
                ptr.offset++;
                ptr.length--;
            }
            else
                break;
        }
        if(trimTrailingSpaces)
            for(int i=ptr.offset+ptr.length-1; i>=ptr.offset; i--) {
                if(ptr.chars[i]==' ') {
                    ptr.length--;
                }
                else
                    break;
            }
        for(int i=ptr.offset+1; i<ptr.offset+ptr.length; i++) {
            if(ptr.chars[i]==' ' && ptr.chars[i-1]==' ') { // remove
                System.arraycopy(ptr.chars, i+1,
                  ptr.chars, i, ptr.length-(i-ptr.offset+1));
                ptr.length--;
                i--;
            }
        }
    }


    private static char replace(char c) {
        switch(c) {
//            case '\u2070': // superscript zero
//            case '\u2080': // subscript zero
//                c=ptr.chars[i]='0';
//            case '\u00b9': // superscript one
//                c=ptr.chars[i]='1';
//                continue;
//            case '\u00b2': // superscript two
//                c=ptr.chars[i]='2';
//                continue;
//            case '\u00b3': // superscript three
//                c=ptr.chars[i]='3';
//                continue;
//            case '\u2074': // superscript four
//            case '\u2084': // subscript four
//                c=ptr.chars[i]='4';
//            case '\u2075': // superscript five
//            case '\u2085': // subscript five
//                c=ptr.chars[i]='5';
//            case '\u2076': // superscript six
//            case '\u2086': // subscript six
//                c=ptr.chars[i]='6';
//            case '\u2077': // superscript seven
//            case '\u2087': // subscript seven
//                c=ptr.chars[i]='7';
//            case '\u2078': // superscript eight
//            case '\u2088': // subscript eight
//                c=ptr.chars[i]='8';
//            case '\u2079': // superscript nine
//            case '\u2089': // subscript nine
//                c=ptr.chars[i]='9';
            case '\u00bc': // one quarter
            case '\u00bd': // one half
            case '\u00be': // three quarters
            case '\u00c6': // uppercase digraph ae
            case '\u00e6': // lowercase digraph ae
            case '\u00de': // uppercase thorn
            case '\u00df': // esszett
            case '\u00fe': // lowercase thorn
            case '\u0132': // uppercase digraph ij
            case '\u0133': // lowercase digraph ij
            case '\u0152': // uppercase digraph oe
            case '\u0153': // lowercase digraph oe
            case '\u0195': // lowercase digraph hv
            case '\u01a2': // uppercase digraph oi
            case '\u01a3': // lowercase digraph oi
            case '\u01be': // lowercase digraph ts
            case '\u01c4': // uppercase letter dz with caron
            case '\u01c5': // titlecase letter dz with caron
            case '\u01c6': // lowercase letter dz with caron
            case '\u01c7': // uppercase letter lj
            case '\u01c8': // titlecase letter lj
            case '\u01c9': // lowercase letter lj
            case '\u01ca': // uppercase letter nj
            case '\u01cb': // titlecase letter nj
            case '\u01cc': // lowercase letter nj
            case '\u01e2': // uppercase digraph ae with macron
            case '\u01e3': // lowercase digraph ae with macron
            case '\u01f1': // uppercase letter dz
            case '\u01f2': // titlecase letter dz
            case '\u01f3': // lowercase letter dz
            case '\u01fc': // uppercase digraph ae with acute
            case '\u01fd': // lowercase digraph ae with acute
            case '\u0222': // uppercase digraph ou
            case '\u0223': // lowercase digraph ou
            case '\ufb00': // lowercase digraph ff
            case '\ufb01': // lowercase digraph fi
            case '\ufb02': // lowercase digraph fl
            case '\ufb03': // lowercase digraph ffi
            case '\ufb04': // lowercase digraph ffl
            case '\ufb05': // lowercase digraph long st
            case '\ufb06': // lowercase digraph st
            case '\u1F80': // GREEK SMALL LETTER ALPHA WITH PSILI AND YPOGEGRAMMENI
            case '\u1F81': // GREEK SMALL LETTER ALPHA WITH DASIA AND YPOGEGRAMMENI
            case '\u1F82': // GREEK SMALL LETTER ALPHA WITH PSILI AND VARIA AND YPOGEGRAMMENI
            case '\u1F83': // GREEK SMALL LETTER ALPHA WITH DASIA AND VARIA AND YPOGEGRAMMENI
            case '\u1F84': // GREEK SMALL LETTER ALPHA WITH PSILI AND OXIA AND YPOGEGRAMMENI
            case '\u1F85': // GREEK SMALL LETTER ALPHA WITH DASIA AND OXIA AND YPOGEGRAMMENI
            case '\u1F86': // GREEK SMALL LETTER ALPHA WITH PSILI AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F87': // GREEK SMALL LETTER ALPHA WITH DASIA AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F88': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND PROSGEGRAMMENI
            case '\u1F89': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND PROSGEGRAMMENI
            case '\u1F8A': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND VARIA AND PROSGEGRAMMENI
            case '\u1F8B': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND VARIA AND PROSGEGRAMMENI
            case '\u1F8C': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND OXIA AND PROSGEGRAMMENI
            case '\u1F8D': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND OXIA AND PROSGEGRAMMENI
            case '\u1F8E': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1F8F': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1F90': // GREEK SMALL LETTER ETA WITH PSILI AND YPOGEGRAMMENI
            case '\u1F91': // GREEK SMALL LETTER ETA WITH DASIA AND YPOGEGRAMMENI
            case '\u1F92': // GREEK SMALL LETTER ETA WITH PSILI AND VARIA AND YPOGEGRAMMENI
            case '\u1F93': // GREEK SMALL LETTER ETA WITH DASIA AND VARIA AND YPOGEGRAMMENI
            case '\u1F94': // GREEK SMALL LETTER ETA WITH PSILI AND OXIA AND YPOGEGRAMMENI
            case '\u1F95': // GREEK SMALL LETTER ETA WITH DASIA AND OXIA AND YPOGEGRAMMENI
            case '\u1F96': // GREEK SMALL LETTER ETA WITH PSILI AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F97': // GREEK SMALL LETTER ETA WITH DASIA AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F98': // GREEK CAPITAL LETTER ETA WITH PSILI AND PROSGEGRAMMENI
            case '\u1F99': // GREEK CAPITAL LETTER ETA WITH DASIA AND PROSGEGRAMMENI
            case '\u1F9A': // GREEK CAPITAL LETTER ETA WITH PSILI AND VARIA AND PROSGEGRAMMENI
            case '\u1F9B': // GREEK CAPITAL LETTER ETA WITH DASIA AND VARIA AND PROSGEGRAMMENI
            case '\u1F9C': // GREEK CAPITAL LETTER ETA WITH PSILI AND OXIA AND PROSGEGRAMMENI
            case '\u1F9D': // GREEK CAPITAL LETTER ETA WITH DASIA AND OXIA AND PROSGEGRAMMENI
            case '\u1F9E': // GREEK CAPITAL LETTER ETA WITH PSILI AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1F9F': // GREEK CAPITAL LETTER ETA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FA0': // GREEK SMALL LETTER OMEGA WITH PSILI AND YPOGEGRAMMENI
            case '\u1FA1': // GREEK SMALL LETTER OMEGA WITH DASIA AND YPOGEGRAMMENI
            case '\u1FA2': // GREEK SMALL LETTER OMEGA WITH PSILI AND VARIA AND YPOGEGRAMMENI
            case '\u1FA3': // GREEK SMALL LETTER OMEGA WITH DASIA AND VARIA AND YPOGEGRAMMENI
            case '\u1FA4': // GREEK SMALL LETTER OMEGA WITH PSILI AND OXIA AND YPOGEGRAMMENI
            case '\u1FA5': // GREEK SMALL LETTER OMEGA WITH DASIA AND OXIA AND YPOGEGRAMMENI
            case '\u1FA6': // GREEK SMALL LETTER OMEGA WITH PSILI AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1FA7': // GREEK SMALL LETTER OMEGA WITH DASIA AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1FA8': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND PROSGEGRAMMENI
            case '\u1FA9': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND PROSGEGRAMMENI
            case '\u1FAA': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND VARIA AND PROSGEGRAMMENI
            case '\u1FAB': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND VARIA AND PROSGEGRAMMENI
            case '\u1FAC': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND OXIA AND PROSGEGRAMMENI
            case '\u1FAD': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND OXIA AND PROSGEGRAMMENI
            case '\u1FAE': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FAF': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FB3': // GREEK SMALL LETTER ALPHA WITH YPOGEGRAMMENI
            case '\u1FBC': // GREEK CAPITAL LETTER ALPHA WITH PROSGEGRAMMENI
            case '\u1FC3': // GREEK SMALL LETTER ETA WITH YPOGEGRAMMENI
            case '\u1FCC': // GREEK CAPITAL LETTER ETA WITH PROSGEGRAMMENI
            case '\u1FF3': // GREEK SMALL LETTER OMEGA WITH YPOGEGRAMMENI
            case '\u1FFC': // GREEK CAPITAL LETTER OMEGA WITH PROSGEGRAMMENI
            case '\u1FB2': // GREEK SMALL LETTER ALPHA WITH VARIA AND YPOGEGRAMMENI
            case '\u1FB4': // GREEK SMALL LETTER ALPHA WITH OXIA AND YPOGEGRAMMENI
            case '\u1FC2': // GREEK SMALL LETTER ETA WITH VARIA AND YPOGEGRAMMENI
            case '\u1FC4': // GREEK SMALL LETTER ETA WITH OXIA AND YPOGEGRAMMENI
            case '\u1FF2': // GREEK SMALL LETTER OMEGA WITH VARIA AND YPOGEGRAMMENI
            case '\u1FF4': // GREEK SMALL LETTER OMEGA WITH OXIA AND YPOGEGRAMMENI
            case '\u1FB7': // GREEK SMALL LETTER ALPHA WITH PERISPOMENI AND YPOGEGRAMMENI
            case '\u1FC7': // GREEK SMALL LETTER ETA WITH PERISPOMENI AND YPOGEGRAMMENI
            case '\u1FF7': // GREEK SMALL LETTER OMEGA WITH PERISPOMENI AND YPOGEGRAMMENI
                return 0; // flag that we need to return multiple characters
            case '\u00c0':
            case '\u00c1':
            case '\u00c2':
            case '\u00c3':
            case '\u00c4':
            case '\u00c5':
            case '\u00e0':
            case '\u00e1':
            case '\u00e2':
            case '\u00e3':
            case '\u00e4':
            case '\u00e5':
            case '\u0100':
            case '\u0101':
            case '\u0102':
            case '\u0103':
            case '\u0104':
            case '\u0105':
            case '\u01cd':
            case '\u01ce':
            case '\u01de':
            case '\u01df':
            case '\u01e0':
            case '\u01e1':
            case '\u01fa':
            case '\u01fb':
            case '\u0200':
            case '\u0201':
            case '\u0202':
            case '\u0203':
            case '\u0226':
            case '\u0227':
            case '\u1e00':
            case '\u1e01':
            case '\u1e9a':
            case '\u1ea0':
            case '\u1ea1':
            case '\u1ea2':
            case '\u1ea3':
            case '\u1ea4':
            case '\u1ea5':
            case '\u1ea6':
            case '\u1ea7':
            case '\u1ea8':
            case '\u1ea9':
            case '\u1eaa':
            case '\u1eab':
            case '\u1eac':
            case '\u1ead':
            case '\u1eae':
            case '\u1eaf':
            case '\u1eb0':
            case '\u1eb1':
            case '\u1eb2':
            case '\u1eb3':
            case '\u1eb4':
            case '\u1eb5':
            case '\u1eb6':
            case '\u1eb7':
                return 'a';
            case '\u0180':
            case '\u0181':
            case '\u0182':
            case '\u0183':
            case '\u0184':
            case '\u0185':
            case '\u1e02':
            case '\u1e03':
            case '\u1e04':
            case '\u1e05':
            case '\u1e06':
            case '\u1e07':
                return 'b';
            case '\u00c7':
            case '\u00e7':
            case '\u0106':
            case '\u0107':
            case '\u0108':
            case '\u0109':
            case '\u010a':
            case '\u010b':
            case '\u010c':
            case '\u010d':
            case '\u0186':
            case '\u0187':
            case '\u0188':
            case '\u1e08':
            case '\u1e09':
                return 'c';
            case '\u00d0': // uppercase eth
            case '\u00f0': // lowercase eth
            case '\u010e':
            case '\u010f':
            case '\u0189':
            case '\u018a':
            case '\u018b':
            case '\u018c':
            case '\u018d':
            case '\u1e0a':
            case '\u1e0b':
            case '\u1e0c':
            case '\u1e0d':
            case '\u1e0e':
            case '\u1e0f':
            case '\u1e10':
            case '\u1e11':
            case '\u1e12':
            case '\u1e13':
            case '\u0110': // uppercase Latin D with stroke
            case '\u0111': // lowercase Latin D with stroke
                return 'd';
            case '\u00c8':
            case '\u00c9':
            case '\u00ca':
            case '\u00cb':
            case '\u00e8':
            case '\u00e9':
            case '\u00ea':
            case '\u00eb':
            case '\u0112':
            case '\u0113':
            case '\u0114':
            case '\u0115':
            case '\u0116':
            case '\u0117':
            case '\u0118':
            case '\u0119':
            case '\u011a':
            case '\u011b':
            case '\u018e':
            case '\u018f':
            case '\u0190':
            case '\u01dd':
            case '\u0204':
            case '\u0205':
            case '\u0206':
            case '\u0207':
            case '\u0228':
            case '\u0229':
            case '\u1e14':
            case '\u1e15':
            case '\u1e16':
            case '\u1e17':
            case '\u1e18':
            case '\u1e19':
            case '\u1e1a':
            case '\u1e1b':
            case '\u1e1c':
            case '\u1e1d':
            case '\u1eb8':
            case '\u1eb9':
            case '\u1eba':
            case '\u1ebb':
            case '\u1ebc':
            case '\u1ebd':
            case '\u1ebe':
            case '\u1ebf':
            case '\u1ec0':
            case '\u1ec1':
            case '\u1ec2':
            case '\u1ec3':
            case '\u1ec4':
            case '\u1ec5':
            case '\u1ec6':
            case '\u1ec7':
                return 'e';
            case '\u0191':
            case '\u0192':
            case '\u1e1e':
            case '\u1e1f':
            case '\u1e9b':
                return 'f';
            case '\u011c':
            case '\u011d':
            case '\u011e':
            case '\u011f':
            case '\u0120':
            case '\u0121':
            case '\u0122':
            case '\u0123':
            case '\u0193':
            case '\u0194':
            case '\u01e4':
            case '\u01e5':
            case '\u01e6':
            case '\u01e7':
            case '\u01f4':
            case '\u01f5':
            case '\u1e20':
            case '\u1e21':
                return 'g';
            case '\u0124':
            case '\u0125':
            case '\u0126':
            case '\u0127':
            case '\u021e':
            case '\u021f':
            case '\u1e22':
            case '\u1e23':
            case '\u1e24':
            case '\u1e25':
            case '\u1e26':
            case '\u1e27':
            case '\u1e28':
            case '\u1e29':
            case '\u1e2a':
            case '\u1e2b':
            case '\u1e96':
                return 'h';
            case '\u00cc':
            case '\u00cd':
            case '\u00ce':
            case '\u00cf':
            case '\u00ec':
            case '\u00ed':
            case '\u00ee':
            case '\u00ef':
            case '\u0128':
            case '\u0129':
            case '\u012a':
            case '\u012b':
            case '\u012c':
            case '\u012d':
            case '\u012e':
            case '\u012f':
            case '\u0130':
            case '\u0131':
            case '\u0196':
            case '\u0197':
            case '\u01cf':
            case '\u01d0':
            case '\u0208':
            case '\u0209':
            case '\u020a':
            case '\u020b':
            case '\u1e2c':
            case '\u1e2d':
            case '\u1e2e':
            case '\u1e2f':
            case '\u1ec8':
            case '\u1ec9':
            case '\u1eca':
            case '\u1ecb':
            case '\u2071': // superscript small letter i
                return 'i';
            case '\u0134':
            case '\u0135':
                return 'j';
            case '\u0136':
            case '\u0137':
            case '\u0138':
            case '\u0198':
            case '\u0199':
            case '\u01e8':
            case '\u01e9':
            case '\u1e30':
            case '\u1e31':
            case '\u1e32':
            case '\u1e33':
            case '\u1e34':
            case '\u1e35':
                return 'k';
            case '\u0139':
            case '\u013a':
            case '\u013b':
            case '\u013c':
            case '\u013d':
            case '\u013e':
            case '\u013f':
            case '\u0140':
            case '\u0141':
            case '\u0142':
            case '\u019a':
            case '\u019b':
            case '\u0234':
            case '\u1e36':
            case '\u1e37':
            case '\u1e38':
            case '\u1e39':
            case '\u1e3a':
            case '\u1e3b':
            case '\u1e3c':
            case '\u1e3d':
            case '\u2113':
                return 'l';
            case '\u019c':
            case '\u1e3e':
            case '\u1e3f':
            case '\u1e40':
            case '\u1e41':
            case '\u1e42':
            case '\u1e43':
                return 'm';
            case '\u00d1':
            case '\u00f1':
            case '\u0143':
            case '\u0144':
            case '\u0145':
            case '\u0146':
            case '\u0147':
            case '\u0148':
            case '\u0149':
            case '\u014a':
            case '\u014b':
            case '\u019d':
            case '\u019e':
            case '\u01f8':
            case '\u01f9':
            case '\u0235':
            case '\u1e44':
            case '\u1e45':
            case '\u1e46':
            case '\u1e47':
            case '\u1e48':
            case '\u1e49':
            case '\u1e4a':
            case '\u1e4b':
            case '\u207f': // superscript latin small letter n
                return 'n';
            case '\u00d2':
            case '\u00d3':
            case '\u00d4':
            case '\u00d5':
            case '\u00d6':
            case '\u00d8':
            case '\u00f2':
            case '\u00f3':
            case '\u00f4':
            case '\u00f5':
            case '\u00f6':
            case '\u00f8':
            case '\u014c':
            case '\u014d':
            case '\u014e':
            case '\u014f':
            case '\u0150':
            case '\u0151':
            case '\u019f':
            case '\u01a0':
            case '\u01a1':
            case '\u01d1':
            case '\u01d2':
            case '\u01fe':
            case '\u01ff':
            case '\u020c':
            case '\u020d':
            case '\u020e':
            case '\u020f':
            case '\u022a':
            case '\u022b':
            case '\u022c':
            case '\u022d':
            case '\u022e':
            case '\u022f':
            case '\u0230':
            case '\u0231':
            case '\u1e4c':
            case '\u1e4d':
            case '\u1e4e':
            case '\u1e4f':
            case '\u1e50':
            case '\u1e51':
            case '\u1e52':
            case '\u1e53':
            case '\u1ecc':
            case '\u1ecd':
            case '\u1ece':
            case '\u1ecf':
            case '\u1ed0':
            case '\u1ed1':
            case '\u1ed2':
            case '\u1ed3':
            case '\u1ed4':
            case '\u1ed5':
            case '\u1ed6':
            case '\u1ed7':
            case '\u1ed8':
            case '\u1ed9':
            case '\u1eda':
            case '\u1edb':
            case '\u1edc':
            case '\u1edd':
            case '\u1ede':
            case '\u1edf':
            case '\u1ee0':
            case '\u1ee1':
            case '\u1ee2':
            case '\u1ee3':
                return 'o';
            case '\u01a4':
            case '\u01a5':
            case '\u1e54':
            case '\u1e55':
            case '\u1e56':
            case '\u1e57':
                return 'p';
            case '\u01ea':
            case '\u01eb':
            case '\u01ec':
            case '\u01ed':
                return 'q';
            case '\u0154':
            case '\u0155':
            case '\u0156':
            case '\u0157':
            case '\u0158':
            case '\u0159':
            case '\u01a6':
            case '\u0210':
            case '\u0211':
            case '\u0212':
            case '\u0213':
            case '\u1e58':
            case '\u1e59':
            case '\u1e5a':
            case '\u1e5b':
            case '\u1e5c':
            case '\u1e5d':
            case '\u1e5e':
            case '\u1e5f':
                return 'r';
            case '\u015a':
            case '\u015b':
            case '\u015c':
            case '\u015d':
            case '\u015e':
            case '\u015f':
            case '\u0160':
            case '\u0161':
            case '\u017f':
            case '\u01a7':
            case '\u01a8':
            case '\u01a9':
            case '\u01aa':
            case '\u0218':
            case '\u0219':
            case '\u1e60':
            case '\u1e61':
            case '\u1e62':
            case '\u1e63':
            case '\u1e64':
            case '\u1e65':
            case '\u1e66':
            case '\u1e67':
            case '\u1e68':
            case '\u1e69':
                return 's';
            case '\u0162':
            case '\u0163':
            case '\u0164':
            case '\u0165':
            case '\u0166':
            case '\u0167':
            case '\u01ab':
            case '\u01ac':
            case '\u01ad':
            case '\u01ae':
            case '\u021a':
            case '\u021b':
            case '\u0236':
            case '\u1e6a':
            case '\u1e6b':
            case '\u1e6c':
            case '\u1e6d':
            case '\u1e6e':
            case '\u1e6f':
            case '\u1e70':
            case '\u1e71':
            case '\u1e97':
                return 't';
            case '\u00d9':
            case '\u00da':
            case '\u00db':
            case '\u00dc':
            case '\u00f9':
            case '\u00fa':
            case '\u00fb':
            case '\u00fc':
            case '\u0168':
            case '\u0169':
            case '\u016a':
            case '\u016b':
            case '\u016c':
            case '\u016d':
            case '\u016e':
            case '\u016f':
            case '\u0170':
            case '\u0171':
            case '\u0172':
            case '\u0173':
            case '\u01af':
            case '\u01b0':
            case '\u01b1':
            case '\u01d3':
            case '\u01d4':
            case '\u01d5':
            case '\u01d6':
            case '\u01d7':
            case '\u01d8':
            case '\u01d9':
            case '\u01da':
            case '\u01db':
            case '\u01dc':
            case '\u0214':
            case '\u0215':
            case '\u0216':
            case '\u0217':
            case '\u1e72':
            case '\u1e73':
            case '\u1e74':
            case '\u1e75':
            case '\u1e76':
            case '\u1e77':
            case '\u1e78':
            case '\u1e79':
            case '\u1e7a':
            case '\u1e7b':
            case '\u1ee4':
            case '\u1ee5':
            case '\u1ee6':
            case '\u1ee7':
            case '\u1ee8':
            case '\u1ee9':
            case '\u1eea':
            case '\u1eeb':
            case '\u1eec':
            case '\u1eed':
            case '\u1eee':
            case '\u1eef':
            case '\u1ef0':
            case '\u1ef1':
                return 'u';
            case '\u01b2':
            case '\u1e7c':
            case '\u1e7d':
            case '\u1e7e':
            case '\u1e7f':
                return 'v';
            case '\u0174':
            case '\u0175':
            case '\u01bf':
            case '\u01f6':
            case '\u01f7':
            case '\u1e80':
            case '\u1e81':
            case '\u1e82':
            case '\u1e83':
            case '\u1e84':
            case '\u1e85':
            case '\u1e86':
            case '\u1e87':
            case '\u1e88':
            case '\u1e89':
            case '\u1e98':
                return 'w';
            case '\u1e8a':
            case '\u1e8b':
            case '\u1e8c':
            case '\u1e8d':
                return 'x';
            case '\u00dd':
            case '\u00fd':
            case '\u00ff':
            case '\u0176':
            case '\u0177':
            case '\u0178':
            case '\u01b3':
            case '\u01b4':
            case '\u0232':
            case '\u0233':
            case '\u1e8e':
            case '\u1e8f':
            case '\u1e99':
            case '\u1ef2':
            case '\u1ef3':
            case '\u1ef4':
            case '\u1ef5':
            case '\u1ef6':
            case '\u1ef7':
            case '\u1ef8':
            case '\u1ef9':
                return 'y';
            case '\u0179':
            case '\u017a':
            case '\u017b':
            case '\u017c':
            case '\u017d':
            case '\u017e':
            case '\u01b5':
            case '\u01b6':
            case '\u01b7':
            case '\u01b8':
            case '\u01b9':
            case '\u01ba':
            case '\u01bb':
            case '\u01ee':
            case '\u01ef':
            case '\u021c':
            case '\u021d':
            case '\u0224':
            case '\u0225':
            case '\u1e90':
            case '\u1e91':
            case '\u1e92':
            case '\u1e93':
            case '\u1e94':
            case '\u1e95':
                return 'z';
            case '\u1F00': //GREEK SMALL LETTER ALPHA WITH PSILI
            case '\u1F01': // GREEK SMALL LETTER ALPHA WITH DASIA
            case '\u1F02': // GREEK SMALL LETTER ALPHA WITH PSILI AND VARIA
            case '\u1F03': // GREEK SMALL LETTER ALPHA WITH DASIA AND VARIA
            case '\u1F04': // GREEK SMALL LETTER ALPHA WITH PSILI AND OXIA
            case '\u1F05': // GREEK SMALL LETTER ALPHA WITH DASIA AND OXIA
            case '\u1F06': // GREEK SMALL LETTER ALPHA WITH PSILI AND PERISPOMENI
            case '\u1F07': // GREEK SMALL LETTER ALPHA WITH DASIA AND PERISPOMENI
            case '\u1F08': // GREEK CAPITAL LETTER ALPHA WITH PSILI
            case '\u1F09': // GREEK CAPITAL LETTER ALPHA WITH DASIA
            case '\u1F0A': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND VARIA
            case '\u1F0B': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND VARIA
            case '\u1F0C': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND OXIA
            case '\u1F0D': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND OXIA
            case '\u1F0E': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND PERISPOMENI
            case '\u1F0F': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND PERISPOMENI
            case '\u1F70': // GREEK SMALL LETTER ALPHA WITH VARIA
            case '\u1F71': // GREEK SMALL LETTER ALPHA WITH OXIA
            case '\u1FB0': // GREEK SMALL LETTER ALPHA WITH VRACHY
            case '\u1FB1': // GREEK SMALL LETTER ALPHA WITH MACRON
            case '\u1FB6': // GREEK SMALL LETTER ALPHA WITH PERISPOMENI
            case '\u1FB8': // GREEK CAPITAL LETTER ALPHA WITH VRACHY
            case '\u1FB9': // GREEK CAPITAL LETTER ALPHA WITH MACRON
            case '\u1FBA': // GREEK CAPITAL LETTER ALPHA WITH VARIA
            case '\u1FBB': // GREEK CAPITAL LETTER ALPHA WITH OXIA
                return '\u03b1'; // lowercase alpha
            case '\u1F10': // GREEK SMALL LETTER EPSILON WITH PSILI
            case '\u1F11': // GREEK SMALL LETTER EPSILON WITH DASIA
            case '\u1F12': // GREEK SMALL LETTER EPSILON WITH PSILI AND VARIA
            case '\u1F13': // GREEK SMALL LETTER EPSILON WITH DASIA AND VARIA
            case '\u1F14': // GREEK SMALL LETTER EPSILON WITH PSILI AND OXIA
            case '\u1F15': // GREEK SMALL LETTER EPSILON WITH DASIA AND OXIA
            case '\u1F18': // GREEK CAPITAL LETTER EPSILON WITH PSILI
            case '\u1F19': // GREEK CAPITAL LETTER EPSILON WITH DASIA
            case '\u1F1A': // GREEK CAPITAL LETTER EPSILON WITH PSILI AND VARIA
            case '\u1F1B': // GREEK CAPITAL LETTER EPSILON WITH DASIA AND VARIA
            case '\u1F1C': // GREEK CAPITAL LETTER EPSILON WITH PSILI AND OXIA
            case '\u1F1D': // GREEK CAPITAL LETTER EPSILON WITH DASIA AND OXIA
            case '\u1F72': // GREEK SMALL LETTER EPSILON WITH VARIA
            case '\u1F73': // GREEK SMALL LETTER EPSILON WITH OXIA
            case '\u1FC8': // GREEK CAPITAL LETTER EPSILON WITH VARIA
            case '\u1FC9': // GREEK CAPITAL LETTER EPSILON WITH OXIA
                return '\u03b5'; // lowercase epsilon
            case '\u1F20': // GREEK SMALL LETTER ETA WITH PSILI
            case '\u1F21': // GREEK SMALL LETTER ETA WITH DASIA
            case '\u1F22': // GREEK SMALL LETTER ETA WITH PSILI AND VARIA
            case '\u1F23': // GREEK SMALL LETTER ETA WITH DASIA AND VARIA
            case '\u1F24': // GREEK SMALL LETTER ETA WITH PSILI AND OXIA
            case '\u1F25': // GREEK SMALL LETTER ETA WITH DASIA AND OXIA
            case '\u1F26': // GREEK SMALL LETTER ETA WITH PSILI AND PERISPOMENI
            case '\u1F27': // GREEK SMALL LETTER ETA WITH DASIA AND PERISPOMENI
            case '\u1F28': // GREEK CAPITAL LETTER ETA WITH PSILI
            case '\u1F29': // GREEK CAPITAL LETTER ETA WITH DASIA
            case '\u1F2A': // GREEK CAPITAL LETTER ETA WITH PSILI AND VARIA
            case '\u1F2B': // GREEK CAPITAL LETTER ETA WITH DASIA AND VARIA
            case '\u1F2C': // GREEK CAPITAL LETTER ETA WITH PSILI AND OXIA
            case '\u1F2D': // GREEK CAPITAL LETTER ETA WITH DASIA AND OXIA
            case '\u1F2E': // GREEK CAPITAL LETTER ETA WITH PSILI AND PERISPOMENI
            case '\u1F2F': // GREEK CAPITAL LETTER ETA WITH DASIA AND PERISPOMENI
            case '\u1F74': // GREEK SMALL LETTER ETA WITH VARIA
            case '\u1F75': // GREEK SMALL LETTER ETA WITH OXIA
            case '\u1FC6': // GREEK SMALL LETTER ETA WITH PERISPOMENI
            case '\u1FCA': // GREEK CAPITAL LETTER ETA WITH VARIA
            case '\u1FCB': // GREEK CAPITAL LETTER ETA WITH OXIA
                return '\u03b7'; // lowercase eta
            case '\u1F30': // GREEK SMALL LETTER IOTA WITH PSILI
            case '\u1F31': // GREEK SMALL LETTER IOTA WITH DASIA
            case '\u1F32': // GREEK SMALL LETTER IOTA WITH PSILI AND VARIA
            case '\u1F33': // GREEK SMALL LETTER IOTA WITH DASIA AND VARIA
            case '\u1F34': // GREEK SMALL LETTER IOTA WITH PSILI AND OXIA
            case '\u1F35': // GREEK SMALL LETTER IOTA WITH DASIA AND OXIA
            case '\u1F36': // GREEK SMALL LETTER IOTA WITH PSILI AND PERISPOMENI
            case '\u1F37': // GREEK SMALL LETTER IOTA WITH DASIA AND PERISPOMENI
            case '\u1F38': // GREEK CAPITAL LETTER IOTA WITH PSILI
            case '\u1F39': // GREEK CAPITAL LETTER IOTA WITH DASIA
            case '\u1F3A': // GREEK CAPITAL LETTER IOTA WITH PSILI AND VARIA
            case '\u1F3B': // GREEK CAPITAL LETTER IOTA WITH DASIA AND VARIA
            case '\u1F3C': // GREEK CAPITAL LETTER IOTA WITH PSILI AND OXIA
            case '\u1F3D': // GREEK CAPITAL LETTER IOTA WITH DASIA AND OXIA
            case '\u1F3E': // GREEK CAPITAL LETTER IOTA WITH PSILI AND PERISPOMENI
            case '\u1F3F': // GREEK CAPITAL LETTER IOTA WITH DASIA AND PERISPOMENI
            case '\u1F76': // GREEK SMALL LETTER IOTA WITH VARIA
            case '\u1F77': // GREEK SMALL LETTER IOTA WITH OXIA
            case '\u1FD0': // GREEK SMALL LETTER IOTA WITH VRACHY
            case '\u1FD1': // GREEK SMALL LETTER IOTA WITH MACRON
            case '\u1FD2': // GREEK SMALL LETTER IOTA WITH DIALYTIKA AND VARIA
            case '\u1FD3': // GREEK SMALL LETTER IOTA WITH DIALYTIKA AND OXIA
            case '\u1FD6': // GREEK SMALL LETTER IOTA WITH PERISPOMENI
            case '\u1FD7': // GREEK SMALL LETTER IOTA WITH DIALYTIKA AND PERISPOMENI
            case '\u1FD8': // GREEK CAPITAL LETTER IOTA WITH VRACHY
            case '\u1FD9': // GREEK CAPITAL LETTER IOTA WITH MACRON
            case '\u1FDA': // GREEK CAPITAL LETTER IOTA WITH VARIA
            case '\u1FDB': // GREEK CAPITAL LETTER IOTA WITH OXIA
                return '\u03b9'; // lowercase iota
            case '\u1F40': // GREEK SMALL LETTER OMICRON WITH PSILI
            case '\u1F41': // GREEK SMALL LETTER OMICRON WITH DASIA
            case '\u1F42': // GREEK SMALL LETTER OMICRON WITH PSILI AND VARIA
            case '\u1F43': // GREEK SMALL LETTER OMICRON WITH DASIA AND VARIA
            case '\u1F44': // GREEK SMALL LETTER OMICRON WITH PSILI AND OXIA
            case '\u1F45': // GREEK SMALL LETTER OMICRON WITH DASIA AND OXIA
            case '\u1F48': // GREEK CAPITAL LETTER OMICRON WITH PSILI
            case '\u1F49': // GREEK CAPITAL LETTER OMICRON WITH DASIA
            case '\u1F4A': // GREEK CAPITAL LETTER OMICRON WITH PSILI AND VARIA
            case '\u1F4B': // GREEK CAPITAL LETTER OMICRON WITH DASIA AND VARIA
            case '\u1F4C': // GREEK CAPITAL LETTER OMICRON WITH PSILI AND OXIA
            case '\u1F4D': // GREEK CAPITAL LETTER OMICRON WITH DASIA AND OXIA
            case '\u1F78': // GREEK SMALL LETTER OMICRON WITH VARIA
            case '\u1F79': // GREEK SMALL LETTER OMICRON WITH OXIA
            case '\u1FF8': // GREEK CAPITAL LETTER OMICRON WITH VARIA
            case '\u1FF9': // GREEK CAPITAL LETTER OMICRON WITH OXIA
                return '\u03bf'; // lowercase omicron
            case '\u1F50': // GREEK SMALL LETTER UPSILON WITH PSILI
            case '\u1F51': // GREEK SMALL LETTER UPSILON WITH DASIA
            case '\u1F52': // GREEK SMALL LETTER UPSILON WITH PSILI AND VARIA
            case '\u1F53': // GREEK SMALL LETTER UPSILON WITH DASIA AND VARIA
            case '\u1F54': // GREEK SMALL LETTER UPSILON WITH PSILI AND OXIA
            case '\u1F55': // GREEK SMALL LETTER UPSILON WITH DASIA AND OXIA
            case '\u1F56': // GREEK SMALL LETTER UPSILON WITH PSILI AND PERISPOMENI
            case '\u1F57': // GREEK SMALL LETTER UPSILON WITH DASIA AND PERISPOMENI
            case '\u1F59': // GREEK CAPITAL LETTER UPSILON WITH DASIA
            case '\u1F5B': // GREEK CAPITAL LETTER UPSILON WITH DASIA AND VARIA
            case '\u1F5D': // GREEK CAPITAL LETTER UPSILON WITH DASIA AND OXIA
            case '\u1F5F': // GREEK CAPITAL LETTER UPSILON WITH DASIA AND PERISPOMENI
            case '\u1F7A': // GREEK SMALL LETTER UPSILON WITH VARIA
            case '\u1F7B': // GREEK SMALL LETTER UPSILON WITH OXIA
            case '\u1FE0': // GREEK SMALL LETTER UPSILON WITH VRACHY
            case '\u1FE1': // GREEK SMALL LETTER UPSILON WITH MACRON
            case '\u1FE2': // GREEK SMALL LETTER UPSILON WITH DIALYTIKA AND VARIA
            case '\u1FE3': // GREEK SMALL LETTER UPSILON WITH DIALYTIKA AND OXIA
            case '\u1FE6': // GREEK SMALL LETTER UPSILON WITH PERISPOMENI
            case '\u1FE7': // GREEK SMALL LETTER UPSILON WITH DIALYTIKA AND PERISPOMENI
            case '\u1FE8': // GREEK CAPITAL LETTER UPSILON WITH VRACHY
            case '\u1FE9': // GREEK CAPITAL LETTER UPSILON WITH MACRON
            case '\u1FEA': // GREEK CAPITAL LETTER UPSILON WITH VARIA
            case '\u1FEB': // GREEK CAPITAL LETTER UPSILON WITH OXIA
                return '\u03c5'; // lowercase upsilon
            case '\u1F60': // GREEK SMALL LETTER OMEGA WITH PSILI
            case '\u1F61': // GREEK SMALL LETTER OMEGA WITH DASIA
            case '\u1F62': // GREEK SMALL LETTER OMEGA WITH PSILI AND VARIA
            case '\u1F63': // GREEK SMALL LETTER OMEGA WITH DASIA AND VARIA
            case '\u1F64': // GREEK SMALL LETTER OMEGA WITH PSILI AND OXIA
            case '\u1F65': // GREEK SMALL LETTER OMEGA WITH DASIA AND OXIA
            case '\u1F66': // GREEK SMALL LETTER OMEGA WITH PSILI AND PERISPOMENI
            case '\u1F67': // GREEK SMALL LETTER OMEGA WITH DASIA AND PERISPOMENI
            case '\u1F68': // GREEK CAPITAL LETTER OMEGA WITH PSILI
            case '\u1F69': // GREEK CAPITAL LETTER OMEGA WITH DASIA
            case '\u1F6A': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND VARIA
            case '\u1F6B': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND VARIA
            case '\u1F6C': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND OXIA
            case '\u1F6D': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND OXIA
            case '\u1F6E': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND PERISPOMENI
            case '\u1F6F': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND PERISPOMENI
            case '\u1F7C': // GREEK SMALL LETTER OMEGA WITH VARIA
            case '\u1F7D': // GREEK SMALL LETTER OMEGA WITH OXIA
            case '\u1FF6': // GREEK SMALL LETTER OMEGA WITH PERISPOMENI
            case '\u1FFA': // GREEK CAPITAL LETTER OMEGA WITH VARIA
            case '\u1FFB': // GREEK CAPITAL LETTER OMEGA WITH OXIA
                return '\u03c9'; // lowercase omega
            case '\u1FE4': // GREEK SMALL LETTER RHO WITH PSILI
            case '\u1FE5': // GREEK SMALL LETTER RHO WITH DASIA
            case '\u1FEC': // GREEK CAPITAL LETTER RHO WITH DASIA
                return '\u03c1'; // lowercase rho
        }
        return Character.toLowerCase(c);
    }

    private static void replaceChar(Pointer ptr, int offset, String s) {
        char[] chars=new char[ptr.length+s.length()-1];
        System.arraycopy(ptr.chars, ptr.offset, chars, 0, offset-ptr.offset);
        System.arraycopy(s.toCharArray(), 0, chars, offset-ptr.offset, s.length());
        if(offset+1<ptr.length)
            System.arraycopy(ptr.chars, offset+1, chars, offset-ptr.offset+s.length(), ptr.length-offset-1);
        ptr.reset(chars);
    }

    private static String replaceMultiple(char c) {
        switch(c) {
            case '\u00bc': // one quarter
                return "1/4";
            case '\u00bd': // one half
                return "1/2";
            case '\u00be': // three quarters
                return "3/4";
            case '\u00c6': // uppercase digraph ae
            case '\u00e6': // lowercase digraph ae
            case '\u01e2': // uppercase digraph ae with macron
            case '\u01e3': // lowercase digraph ae with macron
            case '\u01fc': // uppercase digraph ae with acute
            case '\u01fd': // lowercase digraph ae with acute
                return "ae";
            case '\u01c4': // uppercase letter dz with caron
            case '\u01c5': // titlecase letter dz with caron
            case '\u01c6': // lowercase letter dz with caron
            case '\u01f1': // uppercase letter dz
            case '\u01f2': // titlecase letter dz
            case '\u01f3': // lowercase letter dz
                return "dz";
            case '\u0195': // lowercase digraph hv
                return "hv";
            case '\u0132': // uppercase digraph ij
            case '\u0133': // lowercase digraph ij
                return "ij";
            case '\u01c7': // uppercase letter lj
            case '\u01c8': // titlecase letter lj
            case '\u01c9': // lowercase letter lj
               return "lj";
            case '\u01ca': // uppercase letter nj
            case '\u01cb': // titlecase letter nj
            case '\u01cc': // lowercase letter nj
                return "nj";
            case '\u0152': // uppercase digraph oe
            case '\u0153': // lowercase digraph oe
                return "oe";
            case '\u01a2': // uppercase digraph oi
            case '\u01a3': // lowercase digraph oi
                return "oi";
            case '\u0222': // uppercase digraph ou
            case '\u0223': // lowercase digraph ou
                return "ou";
            case '\u00df': // esszett
                return "ss";
            case '\u00de': // uppercase thorn
            case '\u00fe': // lowercase thorn
                return "th";
            case '\u01be': // lowercase digraph ts
                return "ts";
            case '\ufb00': // lowercase digraph ff
                return "ff";
            case '\ufb01': // lowercase digraph fi
                return "fi";
            case '\ufb02': // lowercase digraph fl
                return "fl";
            case '\ufb03': // lowercase digraph ffi
                return "ffi";
            case '\ufb04': // lowercase digraph ffl
                return "ffl";
            case '\ufb05': // lowercase digraph long st
            case '\ufb06': // lowercase digraph st
                return "st";
            case '\u1F80': // GREEK SMALL LETTER ALPHA WITH PSILI AND YPOGEGRAMMENI
            case '\u1F81': // GREEK SMALL LETTER ALPHA WITH DASIA AND YPOGEGRAMMENI
            case '\u1F82': // GREEK SMALL LETTER ALPHA WITH PSILI AND VARIA AND YPOGEGRAMMENI
            case '\u1F83': // GREEK SMALL LETTER ALPHA WITH DASIA AND VARIA AND YPOGEGRAMMENI
            case '\u1F84': // GREEK SMALL LETTER ALPHA WITH PSILI AND OXIA AND YPOGEGRAMMENI
            case '\u1F85': // GREEK SMALL LETTER ALPHA WITH DASIA AND OXIA AND YPOGEGRAMMENI
            case '\u1F86': // GREEK SMALL LETTER ALPHA WITH PSILI AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F87': // GREEK SMALL LETTER ALPHA WITH DASIA AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F88': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND PROSGEGRAMMENI
            case '\u1F89': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND PROSGEGRAMMENI
            case '\u1F8A': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND VARIA AND PROSGEGRAMMENI
            case '\u1F8B': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND VARIA AND PROSGEGRAMMENI
            case '\u1F8C': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND OXIA AND PROSGEGRAMMENI
            case '\u1F8D': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND OXIA AND PROSGEGRAMMENI
            case '\u1F8E': // GREEK CAPITAL LETTER ALPHA WITH PSILI AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1F8F': // GREEK CAPITAL LETTER ALPHA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FB3': // GREEK SMALL LETTER ALPHA WITH YPOGEGRAMMENI
            case '\u1FBC': // GREEK CAPITAL LETTER ALPHA WITH PROSGEGRAMMENI
            case '\u1FB2': // GREEK SMALL LETTER ALPHA WITH VARIA AND YPOGEGRAMMENI
            case '\u1FB4': // GREEK SMALL LETTER ALPHA WITH OXIA AND YPOGEGRAMMENI
            case '\u1FB7': // GREEK SMALL LETTER ALPHA WITH PERISPOMENI AND YPOGEGRAMMENI
                return "\u03b1\u03b9"; // lowercase alpha lowercase iota
            case '\u1F90': // GREEK SMALL LETTER ETA WITH PSILI AND YPOGEGRAMMENI
            case '\u1F91': // GREEK SMALL LETTER ETA WITH DASIA AND YPOGEGRAMMENI
            case '\u1F92': // GREEK SMALL LETTER ETA WITH PSILI AND VARIA AND YPOGEGRAMMENI
            case '\u1F93': // GREEK SMALL LETTER ETA WITH DASIA AND VARIA AND YPOGEGRAMMENI
            case '\u1F94': // GREEK SMALL LETTER ETA WITH PSILI AND OXIA AND YPOGEGRAMMENI
            case '\u1F95': // GREEK SMALL LETTER ETA WITH DASIA AND OXIA AND YPOGEGRAMMENI
            case '\u1F96': // GREEK SMALL LETTER ETA WITH PSILI AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F97': // GREEK SMALL LETTER ETA WITH DASIA AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1F98': // GREEK CAPITAL LETTER ETA WITH PSILI AND PROSGEGRAMMENI
            case '\u1F99': // GREEK CAPITAL LETTER ETA WITH DASIA AND PROSGEGRAMMENI
            case '\u1F9A': // GREEK CAPITAL LETTER ETA WITH PSILI AND VARIA AND PROSGEGRAMMENI
            case '\u1F9B': // GREEK CAPITAL LETTER ETA WITH DASIA AND VARIA AND PROSGEGRAMMENI
            case '\u1F9C': // GREEK CAPITAL LETTER ETA WITH PSILI AND OXIA AND PROSGEGRAMMENI
            case '\u1F9D': // GREEK CAPITAL LETTER ETA WITH DASIA AND OXIA AND PROSGEGRAMMENI
            case '\u1F9E': // GREEK CAPITAL LETTER ETA WITH PSILI AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1F9F': // GREEK CAPITAL LETTER ETA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FC3': // GREEK SMALL LETTER ETA WITH YPOGEGRAMMENI
            case '\u1FCC': // GREEK CAPITAL LETTER ETA WITH PROSGEGRAMMENI
            case '\u1FC2': // GREEK SMALL LETTER ETA WITH VARIA AND YPOGEGRAMMENI
            case '\u1FC4': // GREEK SMALL LETTER ETA WITH OXIA AND YPOGEGRAMMENI
            case '\u1FC7': // GREEK SMALL LETTER ETA WITH PERISPOMENI AND YPOGEGRAMMENI
                return "\u03b7\u03b9"; //lowercase eta lowercase iota
            case '\u1FA0': // GREEK SMALL LETTER OMEGA WITH PSILI AND YPOGEGRAMMENI
            case '\u1FA1': // GREEK SMALL LETTER OMEGA WITH DASIA AND YPOGEGRAMMENI
            case '\u1FA2': // GREEK SMALL LETTER OMEGA WITH PSILI AND VARIA AND YPOGEGRAMMENI
            case '\u1FA3': // GREEK SMALL LETTER OMEGA WITH DASIA AND VARIA AND YPOGEGRAMMENI
            case '\u1FA4': // GREEK SMALL LETTER OMEGA WITH PSILI AND OXIA AND YPOGEGRAMMENI
            case '\u1FA5': // GREEK SMALL LETTER OMEGA WITH DASIA AND OXIA AND YPOGEGRAMMENI
            case '\u1FA6': // GREEK SMALL LETTER OMEGA WITH PSILI AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1FA7': // GREEK SMALL LETTER OMEGA WITH DASIA AND PERISPOMENI AND YPOGEGRAMMENI
            case '\u1FA8': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND PROSGEGRAMMENI
            case '\u1FA9': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND PROSGEGRAMMENI
            case '\u1FAA': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND VARIA AND PROSGEGRAMMENI
            case '\u1FAB': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND VARIA AND PROSGEGRAMMENI
            case '\u1FAC': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND OXIA AND PROSGEGRAMMENI
            case '\u1FAD': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND OXIA AND PROSGEGRAMMENI
            case '\u1FAE': // GREEK CAPITAL LETTER OMEGA WITH PSILI AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FAF': // GREEK CAPITAL LETTER OMEGA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI
            case '\u1FF3': // GREEK SMALL LETTER OMEGA WITH YPOGEGRAMMENI
            case '\u1FFC': // GREEK CAPITAL LETTER OMEGA WITH PROSGEGRAMMENI
            case '\u1FF2': // GREEK SMALL LETTER OMEGA WITH VARIA AND YPOGEGRAMMENI
            case '\u1FF4': // GREEK SMALL LETTER OMEGA WITH OXIA AND YPOGEGRAMMENI
            case '\u1FF7': // GREEK SMALL LETTER OMEGA WITH PERISPOMENI AND YPOGEGRAMMENI
                return "\u03c9\u03b9"; //lowercase omega lowercase iota
        }
        return null;
    }
}
class Pointer {
    static final String newLine = System.getProperty("line.separator");
    public byte[] data;
    public char[] chars=null;
    public int    hash, length, offset;

    public Pointer() {
    }

    public Pointer(byte[] data) {
        reset(data, 0, data.length);
    }

    public Pointer(byte[] data, int offset) {
        reset(data, offset, 0);
    }

    public Pointer(byte[] data, int offset, int length) {
        reset(data, offset, length);
    }

    public Pointer(String str) {
        reset(str.toCharArray());
    }

    public Pointer(char[] data) {
        reset(data, 0, data.length);
    }

    public Pointer(char[] data, int offset) {
        reset(data, offset, 0);
    }

    public Pointer(char[] data, int offset, int length) {
        reset(data, offset, length);
    }

    @Override
    public Object clone() {
        try{return super.clone();}
        catch(CloneNotSupportedException e) {
            System.out.println(e);
            return null;
        }
    }

    public Pointer copy() {
        return (Pointer)clone();
    }

    public void endOffset(int endOffset) throws IllegalArgumentException {
        if(endOffset<offset)
            throw new IllegalArgumentException("new end ("+endOffset+") less than start("+offset+")");
        length=endOffset-offset+1;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Pointer))
            return false;
        Pointer p=(Pointer)o;
        if(length!=p.length) {
            return false;
        }
        int i, j;
        if(data!=null) {
            for(i=offset, j=p.offset; i<offset+length; i++, j++)
                if(data[i]!=p.data[j])
                    return false;
        }
        else 
            for(i=offset, j=p.offset; i<offset+length; i++, j++)
                if(chars[i]!=p.chars[j])
                    return false;
        return true;
    }

    public void free() {
        data=null;
    }


    public byte[] getByteData() {
        byte[] bytes=new byte[length];
        System.arraycopy(this.data, offset, bytes, 0, length);
        return bytes;
    }


    @Override
    public int hashCode()
    {
        int i = hash;
        if(i == 0) {
            char ac[]=chars;
            int  j=offset, k=length;
            for(int l = 0; l < k; l++)
                i=31*i+ac[j++];
            hash = i;
        }
        return i;
    }


    public int increment(int amount) {
        length+=amount;
        return length;
    }

    public int indexOf(char c) {
        char[] a=new char[1];
        a[0]=c;
        return indexOf(chars, this.offset, length, a, 0, 1, 0);
    }

    public int indexOf(Pointer s) {
        return indexOf(chars, this.offset, length, s.chars, s.offset,
                       s.length, 0);
    }

    public int indexOf(Pointer s, int offset) {
        return indexOf(chars, this.offset, length, s.chars, s.offset,
                       s.length, offset);
    }

    public static int indexOf(char str[], int strOffset, int strLength,
      char frag[], int fragOffset, int fragLength, int startOffset) {
        if(startOffset >= strLength)
            return fragLength != 0 ? -1 : strLength;
        if(startOffset < 0)
            startOffset = 0;
        if(fragLength == 0)
            return startOffset;
        char c = frag[fragOffset];
        int j1 = strOffset + startOffset;
        int k1 = strOffset + (strLength - fragLength);
label0:
        do
        {
            while(j1 <= k1 && str[j1] != c) 
                j1++;
            if(j1 > k1)
                return -1;
            int l1 = j1 + 1;
            int i2 = (l1 + fragLength) - 1;
            int j2 = fragOffset + 1;
            while(l1 < i2) 
                if(str[l1++] != frag[j2++])
                {
                    j1++;
                    continue label0;
                }
            return j1 - strOffset;
        } while(true);
    }

    public void reset() {
        reset((byte[])null, 0, 0);
    }

    public void reset(byte[] data) {
        reset(data, 0, data.length);
    }

    public void reset(byte[] data, int offset) {
        reset(data, offset, 0);
    }

    public final void reset(byte[] data, int offset, int length) {
        hash=0;
        this.data=data;
        this.chars=null;
        this.offset=offset;
        this.length=length;
    }

    public void reset(String str) {
        reset(str.toCharArray());
    }

    public final void reset(char[] data) {
        reset(data, 0, data.length);
    }

    public void reset(char[] data, int offset) {
        reset(data, offset, 0);
    }

    public final void reset(char[] data, int offset, int length) {
        hash=0;
        this.data=null;
        this.chars=data;
        this.offset=offset;
        this.length=length;
    }

    public void reset(Pointer ptr) {
        if(ptr.data!=null)
            reset(ptr.data, ptr.offset, ptr.length);
        else
            reset(ptr.chars, ptr.offset, ptr.length);
    }


    public void squeezeout(int offset) {
        squeezeout(offset, 1);
    }


    public void squeezeout(int offset, int length) {
        if(offset<this.offset || offset+length>this.offset+this.length)
            throw new IllegalArgumentException("offset: "+offset+" plus length: "+length+" out of range: "+this.offset+"-"+(this.offset+this.length-1));
        System.arraycopy(chars, offset+length, chars, offset, this.length-offset-length);
        this.length-=length;
    }


    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("pointer: offset=").append(offset)
          .append(", length=").append(length);
        if(data!=null)
            sb.append(byteArrayToString(data, offset, length));
        if(chars!=null)
            sb.append('\n').append(new String(chars, offset, length));
        return sb.toString();
    }


    public String toStringVar() {
        if(chars==null)
            return null;
        return new String(chars, offset, length);
    }


    public String toBriefString() {
        StringBuilder sb=new StringBuilder();
        sb.append("pointer: ")
            .append("length=").append(length);
        if(data!=null) {
            if (length > 16) { sb.append("\n"); }
            sb.append(byteArrayToString(data, offset, length));
        }
        return sb.toString();
    }
  public static String byteArrayToString(byte array[], int offset, int length) {
    StringBuilder str = new StringBuilder();
    StringBuilder alpha = new StringBuilder();
    int stopat = length + offset;
    char c;
    int type;

    for (int i=1; offset < stopat; offset++,i++) {
      if ((array[offset]&0xff)<16)
        str.append(" 0");
      else
        str.append(" ");
      str.append(Integer.toString(array[offset]&0xff,16));

      c = (char)array[offset];
      type = Character.getType(c);

      //      if (Character.isLetterOrDigit(c) || (c > )
      if (type == Character.CONTROL || type == Character.LINE_SEPARATOR)
        alpha.append('.');
      else
        alpha.append(c);


      if ((i%16)==0) {
                str.append("  ").append(alpha).append(newLine);
        alpha.setLength(0);
      }
    }

        str.append("  ").append(alpha).append(newLine);
    str.append(newLine);

    return str.toString();
  }
}
