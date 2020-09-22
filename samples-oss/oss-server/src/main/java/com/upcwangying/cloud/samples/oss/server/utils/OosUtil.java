/*
 *
 * MIT License
 *
 * Copyright (c) 2020 cloud.upcwangying.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.upcwangying.cloud.samples.oss.server.utils;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author WANGY
 */
public class OosUtil {
    public static final byte[][] OBJ_REGIONS = new byte[][]{
            Bytes.toBytes("1"),
            Bytes.toBytes("4"),
            Bytes.toBytes("7")
    };

    public static final String OBJ_TABLE_PREFIX = "oos_obj_";
    public static final String DIR_TABLE_PREFIX = "oos_dir_";

    public static final String DIR_META_CF = "cf";
    public static final byte[] DIR_META_CF_BYTES = DIR_META_CF.getBytes();
    public static final String DIR_SUBDIR_CF = "sub";
    public static final byte[] DIR_SUBDIR_CF_BYTES = DIR_SUBDIR_CF.getBytes();
    public static final String OBJ_CONT_CF = "c";
    public static final byte[] OBJ_CONT_CF_BYTES = OBJ_CONT_CF.getBytes();
    public static final String OBJ_META_CF = "cf";
    public static final byte[] OBJ_META_CF_BYTES = OBJ_META_CF.getBytes();

    public static final byte[] DIR_SEQID_QUALIFIER = "u".getBytes();
    public static final byte[] OBJ_CONT_QUALIFIER = "c".getBytes();
    public static final byte[] OBJ_LEN_QUALIFIER = "l".getBytes();
    public static final byte[] OBJ_PROPS_QUALIFIER = "p".getBytes();
    public static final byte[] OBJ_MEDIATYPE_QUALIFIER = "m".getBytes();

    public static final String FILE_STORE_ROOT = "/oos";
    public static final int FILE_STORE_THRESHOLD = 20 * 1024 * 1024;

    public static final int OBJ_LIST_MAX_COUNT = 200;
    public static final String BUCKET_DIR_SEQ_TABLE = "oos_dir_seq";
    public static final String BUCKET_DIR_SEQ_CF = "s";
    public static final byte[] BUCKET_DIR_SEQ_CF_BYTES = BUCKET_DIR_SEQ_CF.getBytes();
    public static final byte[] BUCKET_DIR_SEQ_QUALIFIER = "s".getBytes();

    public static final FilterList OBJ_META_SCAN_FILTER = new FilterList(Operator.MUST_PASS_ONE);

    static {
        try {
            byte[][] qualifiers = new byte[][]{OosUtil.DIR_SEQID_QUALIFIER,
                    OosUtil.OBJ_LEN_QUALIFIER,
                    OosUtil.OBJ_MEDIATYPE_QUALIFIER};
            for (byte[] b : qualifiers) {
                Filter filter = new QualifierFilter(CompareOp.EQUAL,
                        new BinaryComparator(b));
                OBJ_META_SCAN_FILTER.addFilter(filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDirTableName(String bucket) {
        return DIR_TABLE_PREFIX + bucket;
    }

    public static String getObjTableName(String bucket) {
        return OBJ_TABLE_PREFIX + bucket;
    }

    public static String[] getDirColumnFamily() {
        return new String[]{DIR_SUBDIR_CF, DIR_META_CF};
    }

    public static String[] getObjColumnFamily() {
        return new String[]{OBJ_META_CF, OBJ_CONT_CF};
    }
}
