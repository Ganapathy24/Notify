package e.sundaraganapathyl.kssolutions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WorksDetail extends RecyclerView.Adapter<WorksDetail.ViewHolder>{
    private List<Works> mDate;
    private String empID,pass;
    private JSONObject mResult;

    public WorksDetail(List<Works> DateList, JSONObject result,String eid,String p1)
    {
        empID = eid;
        mDate = DateList;
        mResult = result;
        pass = p1;

    }

    @Override
    public WorksDetail.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View workView = inflater.inflate(R.layout.onebyone,parent,false);
        ViewHolder viewHolder = new ViewHolder(context,workView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WorksDetail.ViewHolder viewHolder,int position){
        Works  work = mDate.get(position);

        TextView textView = viewHolder.workname;
        textView.setText(work.getDate());
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView workname;
        public Context context;

        public ViewHolder(Context context, View itemview) {
            super(itemview);

            workname = itemview.findViewById(R.id.pos);
            this.context = context;
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                String pos = Integer.toString(position);
                Toast.makeText(context, (mDate.get(position).getDate()),Toast.LENGTH_SHORT).show();
                Intent in = new Intent(context, DayWiseActivity.class);
                in.putExtra("ID",empID);
                in.putExtra("pass",pass);
                in.putExtra("date", mDate.get(position).getDate());
                context.startActivity(in);

            }
        }
    }

}
