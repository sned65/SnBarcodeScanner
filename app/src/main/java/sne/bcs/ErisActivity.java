package sne.bcs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Правила формирования штрих-кода на машиночитаемой форме рецептурного бланка
 * в Единой региональной информационной системе (ЕРИС)
 * льготного лекарственного обеспечения жителей Московской области.
 *
 * https://www.google.ru/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&ved=0ahUKEwjF0cfuq9fNAhUmP5oKHSfpAOgQFggbMAA&url=https%3A%2F%2Fwww.gosuslugi.ru%2Fpgu%2Fsrfile%2F7313295%2Fdownload%3Fversion%3D1&usg=AFQjCNFelCYRvWGAzPUHki8hPkCb4v-P_g&sig2=om3hmcYhwdaUZ1JRAzyKwg&bvm=bv.126130881,d.bGg&cad=rjt
 */
public class ErisActivity extends AppCompatActivity
{
    private static final String TAG = ErisActivity.class.getName();

    public static final String ERIS_DATA = "eris_data";
    // Note: version code contains two backslashes as in the official document
    private static final String VERSIONS = "!\"#$%&()'*+,./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]/\\_abcdefghijklmnopqrstuvwxyz{|}";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egais);

        String eris_data = getIntent().getStringExtra(ERIS_DATA);
        if (eris_data.charAt(0) == 'u')
        {
            decodeInfo(eris_data.substring(2));
        }
    }

    private void decodeInfo(String eris_data)
    {
        byte[] eris_bytes = Base64.decode(eris_data, Base64.DEFAULT);
        Log.i(TAG, "decodeInfo eris_data "+eris_bytes.length+" bytes");
        BitSet bs = BitSet.valueOf(eris_bytes);
        Log.i(TAG, "bs length = "+bs.length());
        BitSet bs_snils = bs.get(28, 28+37);
        Log.i(TAG, "bs_snils = "+bs_snils.toString());
        long snils1 = bs2long1(bs_snils);
        Log.i(TAG, "SNILS 1 = "+snils1);
        long snils2 = bs_snils.toLongArray()[0];
        Log.i(TAG, "SNILS 2 = "+snils2);
        printOrigSnils();
        int offset = 24+3+1+37;
        BitSet fam = bs.get(offset,offset+240);
        byte[] fam_bytes = fam.toByteArray();
        try
        {
            String f = new String(fam_bytes, "koi8-r");
            Log.i(TAG, "Family = "+f);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        offset += 240;
        BitSet name_bs = bs.get(offset,offset+240);
        byte[] name_bytes = name_bs.toByteArray();
        try
        {
            String f = new String(name_bytes, "cp1251");
            Log.i(TAG, "Name   = "+f);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        offset += 240;
        BitSet middle_name_bs = bs.get(offset,offset+240);
        byte[] middle_name_bytes = middle_name_bs.toByteArray();
        try
        {
            String f = new String(middle_name_bytes, "cp1251");
            Log.i(TAG, "Middle = "+f);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }


        int[] starts = new int[] {239};//, 239+240, 239+240+240};
        for (int i : starts)
        {
            try
            {
                Log.i(TAG, "i = "+i);
                BitSet f_bs = bs.get(i, i+8*60);
                Log.i(TAG, "f_bs.len = "+f_bs.length());
                byte[] f_bytes = f_bs.toByteArray();
                Log.i(TAG, "f_bytes len = "+f_bytes.length);

                String f = new String(f_bytes, "cp1251");
                Log.i(TAG, i+" f = "+f);
                f = new String(f_bytes, "iso-8859-5");
                Log.i(TAG, i+" f = "+f);
//                if ("ША".equalsIgnoreCase(f))
//                {
//                    Log.i(TAG, "****** i = "+i);
//                }

                f = new String(f_bytes, "koi8-r");
                Log.i(TAG, i+" f = "+f);
//                if ("ША".equalsIgnoreCase(f))
//                {
//                    Log.i(TAG, "****** i = "+i);
//                }
            }
            catch(Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
        }

/*
        for (int i = 700; i < 1000; i++)
        {
            Log.i(TAG, "i = "+i);
            BitSet f_bs = bs.get(i, i+8*30);
            byte[] f_bytes = f_bs.toByteArray();
            try
            {
                String f = new String(f_bytes, "cp1251");
                Log.i(TAG, i+" f = "+f);
                f = new String(f_bytes, "iso-8859-5");
                Log.i(TAG, i+" f = "+f);

                f = new String(f_bytes, "koi8-r");
                Log.i(TAG, i+" f = "+f);
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
*/
    }

    private void printOrigSnils()
    {
        final long snils = 1473688056;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; ++i)
        {
            if ((snils & (1<<i)) == 0)
            {
                sb.append('0');
            }
            else
            {
                sb.append('1');
            }
        }
        Log.i(TAG, "ORIG SNILS: "+sb.toString());
    }

    private static long bs2long1(BitSet bs)
    {
        byte[] bytes = bs.toByteArray();
        Log.i(TAG, "bs2long1() bytes.length = "+bytes.length);
        long ans = 0;
        int sz = Math.min(8, bytes.length);
        for (int i = 0; i < sz; ++i)
        {
            ans |= (bytes[i] << (8*i));
        }
        return ans;
    }

    private static long bs2long2(BitSet bs)
    {
        byte[] bytes = bs.toByteArray();
        long ans = 0;
        int sz = Math.min(8, bytes.length);
        for (int i = 0; i < sz; ++i)
        {
            ans |= (bytes[i] << (8*i));
        }
        return ans;
    }

    public static boolean isEris(String text)
    {
        if (text == null || text.length() < 2) return false;
        char ch0 = text.charAt(0);
        Log.i(TAG, "ch0 = "+ch0);
        // "m" - для формирования штрих-кода на рецептурном бланке,
        // "u" - для формирования штрих-кода на информационном листе рецепта
        if (ch0 != 'm' && ch0 != 'u') return false;
        String ch1 = text.substring(1,2);
        Log.i(TAG, "ch1 = "+ch1);
        boolean ans = VERSIONS.contains(ch1);
        Log.i(TAG, "ans = "+ans);
        return ans;
    }
}
