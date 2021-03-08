package www.starcom.com.jualanpraktis.api;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import www.starcom.com.jualanpraktis.model.ResponseGetDataBank;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseDataFavorite;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseDataVideo;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseRegister;
import www.starcom.com.jualanpraktis.model_retrofit.ResponseUpdateRekening;
import www.starcom.com.jualanpraktis.model_retrofit.bank_lainnya.ResponseBankLainnya;
import www.starcom.com.jualanpraktis.model_retrofit.bank_populer.ResponseBankPopuler;
import www.starcom.com.jualanpraktis.model_retrofit.batalkan_pesanan.ResponseBatalkanPesanan;
import www.starcom.com.jualanpraktis.model_retrofit.data_check.ResponseDataCheck;
import www.starcom.com.jualanpraktis.model_retrofit.editakun.ResponseInsertEditAkun;
import www.starcom.com.jualanpraktis.model_retrofit.model_bantuan.ResponseDataBantuan;
import www.starcom.com.jualanpraktis.model_retrofit.model_penggunaan_app.ResponseListPanduan;
import www.starcom.com.jualanpraktis.model_retrofit.model_penghasilan_selesai.ResponseDataPenghasilanSelesai;
import www.starcom.com.jualanpraktis.model_retrofit.model_tips.ResponseDataTips;
import www.starcom.com.jualanpraktis.model_retrofit.periode.ResponseDataPeriode;
import www.starcom.com.jualanpraktis.model_retrofit.tukar_pesanan.ResponseTukarPesanan;

public interface ApiService {

    @GET("list-rekening.php")
    Call<ResponseGetDataBank> getDataBank();

    @FormUrlEncoded
    @POST("list-favorit.php")
    Call<ResponseDataFavorite> getDataFavorite(@Field("customer") String customer);

    @GET("list-panduan.php")
    Call<ResponseDataVideo> getDataVideo();

    @FormUrlEncoded
    @POST("signup.php")
    Call<ResponseRegister> register(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("update_rekening.php")
    Call<ResponseUpdateRekening> updateRekening(@Field("id_member") String id_member,
                                                @Field("atas_nama") String atas_nama,
                                                @Field("no_rek") String no_rek,
                                                @Field("nama_bank") String nama_bank);

    @GET("list-bantuan.php")
    Call<ResponseDataBantuan> dataBantuan();

    @GET("list-panduan.php")
    Call<ResponseListPanduan> dataPanduan();

    @GET("list-tips.php")
    Call<ResponseDataTips> dataTips();

    @FormUrlEncoded
    @POST("update_akun.php")
    Call<ResponseInsertEditAkun> editAkun(@Field("id_member") String id_member,
                                          @Field("nama") String nama,
                                          @Field("nama_toko") String nama_toko,
                                          @Field("provinsi") String provinsi,
                                          @Field("kota") String kota,
                                          @Field("kecamatan") String kecamatan,
                                          @Field("alamat") String alamat,
                                          @Field("no_ktp") String no_ktp,
                                          @Field("no_npwp") String no_npwp,
                                          @Field("no_hp") String no_hp,
                                          @Field("email") String email,
                                          @Field("gender") String gender,
                                          @Field("tgl_lahir") String tgl_lahir,
                                          @Field("status_kawin") String status_kawin,
                                          @Field("jumlah_anak") String jumlah_anak,
                                          @Field("pendidikan") String pendidikan,
                                          @Field("pekerjaan") String pekerjaan,
                                          @Field("penghasilan") String penghasilan,
                                          @Field("password") String password);

    @FormUrlEncoded
    @POST("transaksi_selesai.php")
    Call<ResponseDataPenghasilanSelesai> penghasilanSelesai(@Field("customer") String customer);

//    @FormUrlEncoded
//    @POST("data-check.php")
//    Call<ResponseDataCheck> dataCheck(@Field("id_member") String id_member);

    @GET("list-bank-populer.php")
    Call<ResponseBankPopuler> bankPopuler();

    @GET("list-bank-lainnya.php")
    Call<ResponseBankLainnya> bankLainnya();

    @GET("date-picker.php")
    Call<ResponseDataPeriode> dataPeriode();

    @Multipart
    @POST("batalkan_pesanan.php")
    Call<ResponseBatalkanPesanan> batalkanPesanan(@Part("id_transaksi") RequestBody id_transaksi,
                                                  @Part("id_member") RequestBody id_member,
                                                  @Part("alasan") RequestBody alasan,
                                                  @Part MultipartBody.Part file1,
                                                  @Part MultipartBody.Part file2,
                                                  @Part MultipartBody.Part file3);

    @Multipart
    @POST("retur.php")
    Call<ResponseTukarPesanan> tukarPesanan(@Query("nomor[]") ArrayList<String> nomor,
                                            @Part("alasan") RequestBody alasan,
                                            @Part("id_transaksi") RequestBody id_transaksi,
                                            @Part MultipartBody.Part file1,
                                            @Part MultipartBody.Part file2,
                                            @Part MultipartBody.Part file3);

    @FormUrlEncoded
    @POST("data-check.php")
    Call<ResponseDataCheck> dataCheck(@Field("id_member") String id_member);

}
