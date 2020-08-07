package e.sundaraganapathyl.kssolutions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DayWorkDetails extends RecyclerView.Adapter<DayWorkDetails.ViewHolder> {

    private List<Activity> mWork;
    private JSONObject mResult;
    ArrayList<StoreData> mStore = new ArrayList<StoreData>();
    private String EmpID,cDate;

    public DayWorkDetails(String Id,ArrayList<Activity> WorkList, JSONObject result,String date)
    {
        EmpID = Id;
        mWork = WorkList;
        mResult = result;
        cDate = date;
    }
    public List<StoreData> getList()
    {
        return  mStore;
    }

    @Override
    public DayWorkDetails.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View workView = inflater.inflate(R.layout.onebyone,parent,false);
        DayWorkDetails.ViewHolder viewHolder = new DayWorkDetails.ViewHolder(context,workView);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(DayWorkDetails.ViewHolder viewHolder,int position){
        Activity  work = mWork.get(position);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        TextView textView = viewHolder.workname;
        textView.setText(work.getWork());
        if ((dtf.format(now).equals(cDate) == false)) {
            if (work.getIsComplete() == 0) {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.parseColor("#d96c6c"));
            } else {
                textView.setTextColor(Color.WHITE);
                textView.setClickable(false);
                textView.setBackgroundColor(Color.parseColor("#6ccc8a"));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mWork.size();
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
            final int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                String pos = Integer.toString(position);
                Log.d("Work Array",mWork.toString());
                Toast.makeText(context, (mWork.get(position).getWork()),Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                View mView = LayoutInflater.from(context).inflate(R.layout.dialog, null);

                final EditText actual_time = mView.findViewById(R.id.actual_time);
                final EditText WorkRemark = mView.findViewById(R.id.workRemark);
                final EditText dependency = mView.findViewById(R.id.dependancy);
                final int[] isComplete = {0};
                Button btn_cancel = mView.findViewById(R.id.btn_cancel);
                Button btn_okay = mView.findViewById(R.id.btn_okay);
                final ToggleButton toggle =mView.findViewById(R.id.toggle);
                alert.setView(mView);
                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @SuppressLint("ResourceAsColor")
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            toggle.setBackgroundColor(Color.parseColor("#21ada0"));
                            WorkRemark.setVisibility(View.VISIBLE);
                            actual_time.setVisibility(View.VISIBLE);
                            isComplete[0] = 1;
                        }
                        else{
                            toggle.setBackgroundColor(Color.parseColor("#fa4b4b"));
                            WorkRemark.setVisibility(View.INVISIBLE);
                            actual_time.setVisibility(View.INVISIBLE);
                            isComplete[0] =0;
                        }
                    }
                });

                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String A_time,WRemark;
                        if (isComplete[0] == 0){
                             A_time = "0";
                             WRemark ="0";
                        }
                        else {
                             A_time = actual_time.getText().toString();
                             WRemark = WorkRemark.getText().toString();
                        }
                        String Dependency = dependency.getText().toString();
                        String workID = mWork.get(position).getWorkID();
                        String Complete = String.valueOf(isComplete[0]);
                        mStore.add(new StoreData(EmpID,workID,A_time,WRemark,Dependency,Complete));
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }

            }
        }
    }
