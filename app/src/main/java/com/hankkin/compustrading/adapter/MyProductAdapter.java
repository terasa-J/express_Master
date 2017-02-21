package com.hankkin.compustrading.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hankkin.compustrading.R;
import com.hankkin.compustrading.Utils.BaseUrl;
import com.hankkin.compustrading.Utils.HankkinUtils;
import com.hankkin.compustrading.model.Product;
import com.hankkin.compustrading.mvp_del.IDelView;
import com.hankkin.compustrading.mvp_del.presenter.DelPresenter;
import com.hankkin.compustrading.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *
 */
public class MyProductAdapter extends BaseAdapter implements IDelView {

    private int selectID=0;

    private OnMyCheckChangedListener mCheckChange;



    private List<Product> data;
    private LayoutInflater inflater;
    private Context context;
    private View productView;
    private int count;

    //判断是删除我的发布，还是我的收藏
    private String del=null;

    //服务器端接口
    private String url=BaseUrl.baseUrl+"delMyProduct";
    int cuurentPosition=0;
    DelPresenter delPresenter =new DelPresenter(this);




    public MyProductAdapter(View productView, Context context, int count, List<Product> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.productView=productView;
        this.count=count;
    }

    public MyProductAdapter(String del,Context context, List<Product> data) {
        this.del=del;
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        cuurentPosition=position;
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.me_item,null);
            holder.ivProduct = (ImageView) convertView.findViewById(R.id.iv_product);
            holder.rivUserIcon = (RoundedImageView) convertView.findViewById(R.id.riv_usericon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvPubTime = (TextView) convertView.findViewById(R.id.tv_pub_time);
            holder.tvSchool = (TextView) convertView.findViewById(R.id.tv_school);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            //删除
            holder.tvDel= (TextView) convertView.findViewById(R.id.tv_del);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Product product = data.get(position);

        holder.tvSchool.setText(product.getSchool());
        holder.tvPubTime.setText(product.getCreateat());

        if (!TextUtils.isEmpty(product.getNickname())){
            holder.tvName.setText(product.getNickname());
        }
        else {
            holder.tvName.setText(product.getTel());
        }
        if (!TextUtils.isEmpty(product.getUserIconUrl())){
            ImageLoader.getInstance().displayImage(BaseUrl.baseUrl+product.getUserIconUrl(),holder.rivUserIcon);
        }
        else {
            holder.rivUserIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.defaut));
        }
        holder.tvPrice.setText("报酬 ￥"+product.getPrice());
        holder.tvContent.setText(product.getContent());

        ImageLoader.getInstance().displayImage(BaseUrl.baseUrl+product.getProductUrl(), holder.ivProduct);

        //删除按钮
        holder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                //根据接口删除服务器数据
                //presenter 传入id
                delPresenter.delProduct(product.getProductid(),del);

            }
        });

        return convertView;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void showSuccess(String message) {

        HankkinUtils.showLToast(context,message);
        data.remove(cuurentPosition);  //将集合中的数据删除
        MyProductAdapter.this.notifyDataSetChanged();//更新适配器

    }

    @Override
    public void showFailedError() {
        HankkinUtils.showLToast(context,"删除失败");
    }

    class ViewHolder{
        TextView tvPrice,tvPubTime,tvName,tvSchool,tvContent,tvDel;
        ImageView ivProduct;
        RoundedImageView rivUserIcon;
    }



    // 自定义的选中方法
    public void setSelectID(int position) {

    }
    // 回調接口
    public interface OnMyCheckChangedListener {
        void setSelectID(int selectID);
    }
    // 回调函数，类似OnClickListener吧
    public void setOncheckChanged(OnMyCheckChangedListener l) {
        mCheckChange = l;
    }
}
