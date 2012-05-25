require 'test_helper'

class DrawUsersControllerTest < ActionController::TestCase
  setup do
    @draw_user = draw_users(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:draw_users)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create draw_user" do
    assert_difference('DrawUser.count') do
      post :create, :draw_user => { :id_draw => @draw_user.id_draw, :id_user => @draw_user.id_user }
    end

    assert_redirected_to draw_user_path(assigns(:draw_user))
  end

  test "should show draw_user" do
    get :show, :id => @draw_user
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @draw_user
    assert_response :success
  end

  test "should update draw_user" do
    put :update, :id => @draw_user, :draw_user => { :id_draw => @draw_user.id_draw, :id_user => @draw_user.id_user }
    assert_redirected_to draw_user_path(assigns(:draw_user))
  end

  test "should destroy draw_user" do
    assert_difference('DrawUser.count', -1) do
      delete :destroy, :id => @draw_user
    end

    assert_redirected_to draw_users_path
  end
end
